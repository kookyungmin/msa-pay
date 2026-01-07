package net.happykoo.money.application.service;

import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.happykoo.common.annotation.UseCase;
import net.happykoo.common.latch.CountDownLatchManager;
import net.happykoo.common.logging.LoggingProducer;
import net.happykoo.common.task.RechargingMoneyTask;
import net.happykoo.common.task.SubTask;
import net.happykoo.common.task.TaskStatus;
import net.happykoo.money.application.axon.command.AxonCreateMemberMoneyCommand;
import net.happykoo.money.application.axon.command.AxonIncreaseMemberMoneyCommand;
import net.happykoo.money.application.port.in.CreateMemberMoneyUseCase;
import net.happykoo.money.application.port.in.IncreaseMoneyRequestUseCase;
import net.happykoo.money.application.port.in.ProcessRechargingMoneyResultTaskUseCase;
import net.happykoo.money.application.port.in.command.CreateMemberMoneyCommand;
import net.happykoo.money.application.port.in.command.IncreaseMoneyRequestCommand;
import net.happykoo.money.application.port.in.command.RechargeMoneyRequestCommand;
import net.happykoo.money.application.port.out.ChangeMemberMoneyPort;
import net.happykoo.money.application.port.out.FindMemberMoneyPort;
import net.happykoo.money.application.port.out.SaveMoneyChangingRequestPort;
import net.happykoo.money.application.port.out.SendRechargingMoneyTaskPort;
import net.happykoo.money.domain.MemberMoney;
import net.happykoo.money.domain.MoneyChangingRequest;
import net.happykoo.money.domain.MoneyChangingRequestType;
import net.happykoo.money.domain.axon.event.AxonRechargeMoneyEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;

@Slf4j
@UseCase
@RequiredArgsConstructor
@Transactional
public class MoneyChangingRequestService implements IncreaseMoneyRequestUseCase,
    ProcessRechargingMoneyResultTaskUseCase,
    CreateMemberMoneyUseCase {

  private final SaveMoneyChangingRequestPort saveMoneyChangingRequestPort;
  private final ChangeMemberMoneyPort changeMemberMoneyPort;
  private final FindMemberMoneyPort findMemberMoneyPort;
  private final SendRechargingMoneyTaskPort sendRechargingMoneyTaskPort;
  private final CountDownLatchManager countDownLatchManager;
  private final LoggingProducer loggingProducer;
  private final CommandGateway commandGateway;
  private final EventGateway eventGateway;

  @Override
  public MoneyChangingRequest increaseMoneyRequest(IncreaseMoneyRequestCommand command) {
    //원래 비즈니스 로직
    //a. 증액을 위한 기록 -> MoneyChangingRequest 생성
    //b. 고객 정보가 정상인지 확인 (멤버 서비스) -> 아니면 예외 발생
    //c. 고객의 연동된 계좌가 있고, 정상적인지 확인 (잔액이 충분한지도) (뱅킹 서비스) -> 아니면 예외 발생
    //d. 법인 계좌 상태가 정상인지 확인 (뱅킹 서비스) -> 아니면 예외 발생
    //e. 증액을 위한 기록 -> MoneyChangingRequest 생성
    //f. 펌뱅킹 수행 (고객의 연동된 계좌 -> 해피페이 법인 계좌) (뱅킹 서비스)
    //g. 펌뱅킹 성공하면, 멤버의 머니도 증액, 실패하면 상태 변경

    //0. 증액을 위한 기록 -> MoneyChangingRequest 생성
    var moneyChangingRequest = saveMoneyChangingRequestPort.createMoneyChangingRequest(
        new MoneyChangingRequest.TargetMembershipId(command.getTargetMembershipId()),
        new MoneyChangingRequest.RequestType(MoneyChangingRequestType.INCREASE),
        new MoneyChangingRequest.MoneyAmount(command.getMoneyAmount())
    );

    //1. SubTask, Task 생성 (펌뱅킹 생략)
    var task = generateRechargingMoneyTask(command);

    //2. Kafka Cluster Produce
    sendRechargingMoneyTaskPort.sendRechargingMoneyTask(task);

    //3. Kafka Task 가 모두 완료될 때까지 wait
    try {
      countDownLatchManager.addCountDownLatch(task.getTaskId());
      countDownLatchManager.getCountDownLatch(task.getTaskId()).await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    //4. 성공하면, 멤버의 머니도 증액, 실패하면 상태 변경
    var isSuccess = "success".equals(countDownLatchManager.getDataForKey(task.getTaskId()));
    if (isSuccess) {
      changeMemberMoneyPort.increaseMemberMoney(
          new MemberMoney.MembershipId(command.getTargetMembershipId()),
          new MemberMoney.Balance(command.getMoneyAmount())
      );
      moneyChangingRequest.success();
    } else {
      moneyChangingRequest.failed("failed to increase money.");
    }

    return saveMoneyChangingRequestPort.updateMoneyChangingStatus(
        new MoneyChangingRequest.MoneyChangingRequestId(
            moneyChangingRequest.getMoneyChangingRequestId()),
        new MoneyChangingRequest.RequestStatus(moneyChangingRequest.getRequestStatus()),
        new MoneyChangingRequest.Message(moneyChangingRequest.getMessage())
    );
  }

  @Override
  public void processRechargingMoneyResultTask(RechargingMoneyTask task) {
    var taskId = task.getTaskId();
    var isSuccess = task.getSubTasks()
        .stream()
        .allMatch(subTask -> subTask.getStatus() == TaskStatus.SUCCESS);

    if (isSuccess) {
      loggingProducer.sendMessage(taskId, "task success");
      countDownLatchManager.setDataForKey(taskId, "success");
    } else {
      loggingProducer.sendMessage(taskId, "failed");
      countDownLatchManager.setDataForKey(taskId, "failed");
    }
    countDownLatchManager.getCountDownLatch(taskId).countDown();
  }

  private RechargingMoneyTask generateRechargingMoneyTask(IncreaseMoneyRequestCommand command) {
    var task = RechargingMoneyTask.builder()
        .taskId(UUID.randomUUID().toString())
        .taskName("Increase Money Request : 머니 충전")
        .membershipId(command.getTargetMembershipId())
        .moneyAmount(command.getMoneyAmount())
        .toBankName("happymoney")
        .toBankAccountNumber("1234-4567-8910")
        .build();

    var validMemberTask = SubTask.builder()
        .taskId(task.getTaskId())
        .subTaskName("validMemberTask : 멤버쉽 유효성 검사")
        .membershipId(command.getTargetMembershipId())
        .taskType("membership")
        .status(TaskStatus.READY)
        .build();

    var validBankingAccountTask = SubTask.builder()
        .taskId(task.getTaskId())
        .subTaskName("validBankingAccountTask : 뱅킹 계좌 유효성 검사")
        .membershipId(command.getTargetMembershipId())
        .taskType("banking")
        .status(TaskStatus.READY)
        .build();

    task.addSubTask(validMemberTask);
    task.addSubTask(validBankingAccountTask);

    return task;
  }

  @Override
  public void createMemberMoney(CreateMemberMoneyCommand command) {
    boolean isExists = findMemberMoneyPort.existsMemberMoneyByMembershipId(
        new MemberMoney.MembershipId(command.getMembershipId())
    );

    if (isExists) {
      throw new IllegalArgumentException("entity already exists : " + command.getMembershipId());
    }

    AxonCreateMemberMoneyCommand axonCreateMemberMoneyCommand = new AxonCreateMemberMoneyCommand(
        UUID.randomUUID().toString(),
        Long.parseLong(command.getMembershipId())
    );

    //1. commandGateway.send() 호출
    //2. CommandBus가 Aggregate의 @CommandHandler 호출
    //3. CommandHandler가 Event를 생성
    //4. AggregateLifecycle.apply(event) 호출
    //5. Axon이 즉시 @EventSourcingHandler를 호출해서 Aggregate 상태를 변경
    //6. Event가 EventStore에 영속화
    //7. 트랜잭션 커밋 후 EventBus로 publish
    //8. EventProcessor가 EventStore로부터 이벤트를 consume하고
    //   @EventHandler / @SagaEventHandler에서
    //   Projection 갱신, 외부 연동, 다음 Command 발행 등의
    //   후속 처리를 비동기로 수행

    commandGateway.send(axonCreateMemberMoneyCommand)
        .whenComplete((r, ex) -> {
          if (ex != null) {
            log.error("CreateMemberMoney failed", ex);
          }
        });
  }

  @Override
  public void increaseMoneyRequestByEvent(
      IncreaseMoneyRequestCommand command) {
    var memberMoney = findMemberMoneyPort.findMemberMoneyByMembershipId(
        new MemberMoney.MembershipId(command.getTargetMembershipId())
    );

    AxonIncreaseMemberMoneyCommand axonIncreaseMemberMoneyCommand = new AxonIncreaseMemberMoneyCommand(
        memberMoney.getEventStreamId(),
        Long.parseLong(command.getTargetMembershipId()),
        command.getMoneyAmount(),
        null
    );

    commandGateway.send(axonIncreaseMemberMoneyCommand)
        .whenComplete((r, ex) -> {
          if (ex != null) {
            log.error("IncreaseMoneyRequestByEvent failed", ex);
          }
        });
  }

  //뱅킹 서비스 사가 연동 용
  @Override
  public void rechargeMoneyRequestByEvent(RechargeMoneyRequestCommand command) {
    var memberMoney = findMemberMoneyPort.findMemberMoneyByMembershipId(
        new MemberMoney.MembershipId(command.getTargetMembershipId())
    );

    var axonRechargeMoneyEvent = new AxonRechargeMoneyEvent(
        UUID.randomUUID().toString(),
        memberMoney.getEventStreamId(),
        Long.parseLong(command.getTargetMembershipId()),
        command.getMoneyAmount()
    );

    //TODO: 원래는 aggregate 생성 후 aggregate 만이 event 발행해야 함
    eventGateway.publish(axonRechargeMoneyEvent);
  }
}
