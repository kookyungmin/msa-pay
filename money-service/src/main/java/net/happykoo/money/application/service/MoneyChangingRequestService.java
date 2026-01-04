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
import net.happykoo.money.application.port.in.IncreaseMoneyRequestUseCase;
import net.happykoo.money.application.port.in.ProcessRechargingMoneyResultTaskUseCase;
import net.happykoo.money.application.port.in.command.IncreaseMoneyRequestCommand;
import net.happykoo.money.application.port.out.ChangeMemberMoneyPort;
import net.happykoo.money.application.port.out.SaveMoneyChangingRequestPort;
import net.happykoo.money.application.port.out.SendRechargingMoneyTaskPort;
import net.happykoo.money.domain.MemberMoney;
import net.happykoo.money.domain.MoneyChangingRequest;
import net.happykoo.money.domain.MoneyChangingRequestType;

@Slf4j
@UseCase
@RequiredArgsConstructor
@Transactional
public class MoneyChangingRequestService implements IncreaseMoneyRequestUseCase,
    ProcessRechargingMoneyResultTaskUseCase {

  private final SaveMoneyChangingRequestPort saveMoneyChangingRequestPort;
  private final ChangeMemberMoneyPort changeMemberMoneyPort;
  private final SendRechargingMoneyTaskPort sendRechargingMoneyTaskPort;
  private final CountDownLatchManager countDownLatchManager;
  private final LoggingProducer loggingProducer;

  @Override
  public MoneyChangingRequest increaseMoneyRequest(IncreaseMoneyRequestCommand command) {
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
    }

    return saveMoneyChangingRequestPort.updateMoneyChangingStatus(
        new MoneyChangingRequest.MoneyChangingRequestId(
            moneyChangingRequest.getMoneyChangingRequestId()),
        new MoneyChangingRequest.RequestStatus(moneyChangingRequest.getRequestStatus())
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

//  @Override
//  public MoneyChangingRequest increaseMoneyRequest(IncreaseMoneyRequestCommand command) {
//    //1. 고객 정보가 정상인지 확인 (멤버 서비스) -> 아니면 예외 발생
//    //2. 고객의 연동된 계좌가 있고, 정상적인지 확인 (잔액이 충분한지도) (뱅킹 서비스) -> 아니면 예외 발생
//    //3. 법인 계좌 상태가 정상인지 확인 (뱅킹 서비스) -> 아니면 예외 발생
//
//    //4. 증액을 위한 기록 -> MoneyChangingRequest 생성
//    var moneyChangingRequest = saveMoneyChangingRequestPort.createMoneyChangingRequest(
//        new MoneyChangingRequest.TargetMembershipId(command.getTargetMembershipId()),
//        new MoneyChangingRequest.RequestType(MoneyChangingRequestType.INCREASE),
//        new MoneyChangingRequest.MoneyAmount(command.getMoneyAmount())
//    );
//
//    //5. 펌뱅킹 수행 (고객의 연동된 계좌 -> 해피페이 법인 계좌) (뱅킹 서비스)
//
//    //6. 펌뱅킹 성공하면, 멤버의 머니도 증액, 실패하면 상태 변경
//    boolean isSuccess = true;
//    if (isSuccess) {
//      changeMemberMoneyPort.increaseMemberMoney(
//          new MemberMoney.MembershipId(command.getTargetMembershipId()),
//          new MemberMoney.Balance(command.getMoneyAmount())
//      );
//      moneyChangingRequest.success();
//    }
//
//    return saveMoneyChangingRequestPort.updateMoneyChangingStatus(
//        new MoneyChangingRequest.MoneyChangingRequestId(
//            moneyChangingRequest.getMoneyChangingRequestId()),
//        new MoneyChangingRequest.RequestStatus(moneyChangingRequest.getRequestStatus())
//    );
//  }
}
