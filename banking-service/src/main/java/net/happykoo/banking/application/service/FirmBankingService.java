package net.happykoo.banking.application.service;

import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.happykoo.banking.application.axon.command.AxonCreateFirmBankingRequestCommand;
import net.happykoo.banking.application.axon.command.AxonUpdateFirmBankingRequestStatusCommand;
import net.happykoo.banking.application.port.in.RequestFirmBankingUseCase;
import net.happykoo.banking.application.port.in.UpdateFirmBankingStatusUseCase;
import net.happykoo.banking.application.port.in.command.RequestFirmBankingCommand;
import net.happykoo.banking.application.port.in.command.UpdateFirmBankingStatusCommand;
import net.happykoo.banking.application.port.out.RequestBankAccountInfoPort;
import net.happykoo.banking.application.port.out.RequestFirmBankingPort;
import net.happykoo.banking.application.port.out.SaveFirmBankingRequestPort;
import net.happykoo.banking.application.port.out.payload.BankAccountPayload;
import net.happykoo.banking.application.port.out.payload.FirmBankingPayload;
import net.happykoo.banking.domain.FirmBankingRequest;
import net.happykoo.banking.domain.FirmBankingRequest.FirmBankingRequestId;
import net.happykoo.banking.domain.FirmBankingRequest.FromBankAccountNumber;
import net.happykoo.banking.domain.FirmBankingRequest.FromBankName;
import net.happykoo.banking.domain.FirmBankingRequest.Message;
import net.happykoo.banking.domain.FirmBankingRequest.MoneyAmount;
import net.happykoo.banking.domain.FirmBankingRequest.RequestStatus;
import net.happykoo.banking.domain.FirmBankingRequest.ToBankAccountNumber;
import net.happykoo.banking.domain.FirmBankingRequest.ToBankName;
import net.happykoo.banking.domain.axon.event.AxonFirmBankingRequestEvent;
import net.happykoo.common.annotation.UseCase;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class FirmBankingService implements RequestFirmBankingUseCase,
    UpdateFirmBankingStatusUseCase {

  private final SaveFirmBankingRequestPort saveFirmBankingRequestPort;
  private final RequestBankAccountInfoPort requestBankAccountInfoPort;
  private final RequestFirmBankingPort requestFirmBankingPort;

  private final CommandGateway commandGateway;
  private final EventGateway eventGateway;

  @Override
  @Transactional
  public FirmBankingRequest requestFirmBanking(RequestFirmBankingCommand command) {

    //1. A -> B 계좌 요청에 대해 DB write
    var firmBankingRequest = saveFirmBankingRequestPort.createFirmBankingRequest(
        new FromBankName(command.getFromBankName()),
        new FromBankAccountNumber(command.getFromBankAccountNumber()),
        new ToBankName(command.getToBankName()),
        new ToBankAccountNumber(command.getToBankAccountNumber()),
        new MoneyAmount(command.getMoneyAmount())
    );

    //2. 계좌 유효 상태 확인
    var fromBankData = requestBankAccountInfoPort.requestBankAccountInfo(
        new BankAccountPayload(command.getFromBankName(), command.getFromBankAccountNumber())
    );
    var toBankData = requestBankAccountInfoPort.requestBankAccountInfo(
        new BankAccountPayload(command.getToBankName(), command.getToBankAccountNumber())
    );

    if (!fromBankData.isValid() || !toBankData.isValid()) {
      firmBankingRequest.failed("account is not valid.");
    } else {
      //3. 외부 은행에 펌뱅킹 요청
      var result = requestFirmBankingPort.requestFirmBanking(new FirmBankingPayload(
          command.getFromBankName(),
          command.getFromBankAccountNumber(),
          command.getToBankName(),
          command.getToBankAccountNumber(),
          command.getMoneyAmount()
      ));
      if (result.isSuccess()) {
        firmBankingRequest.success();
      } else {
        firmBankingRequest.failed("failed to request firm banking.");
      }
    }

    //4. 결과에 따라 1번 작성했던 상태 update
    return saveFirmBankingRequestPort.updateFirmBankingStatus(
        new FirmBankingRequestId(firmBankingRequest.getFirmBankingRequestId()),
        new RequestStatus(firmBankingRequest.getRequestStatus()),
        new Message(firmBankingRequest.getMessage())
    );
  }

  @Override
  public void requestFirmBankingByEvent(RequestFirmBankingCommand command) {

    AxonFirmBankingRequestEvent event = new AxonFirmBankingRequestEvent(
        UUID.randomUUID().toString(),
        command.getFromBankName(),
        command.getFromBankAccountNumber(),
        command.getToBankName(),
        command.getToBankAccountNumber(),
        command.getMoneyAmount(),
        command.getExternalRequestId()
    );

    //1. CreateFirmBankingRequestEvent -> EventStore 에 저장
    //2. Projection -> JPA 로 읽기용 DB 에 데이터 저장
    //3. Saga -> 계좌 유효상태 확인 및 펌뱅킹 실행 -> 성공/실패 여부에 따라 분기하여 Command 발송
    //4. Projection -> JPA 로 읽기용 DB 에 status 저장
    //TODO: 원래는 aggregate 생성 후 aggregate 만이 event 발행해야 함
    eventGateway.publish(event);
  }

  @Override
  public void updateFirmBankingStatus(UpdateFirmBankingStatusCommand command) {
    AxonUpdateFirmBankingRequestStatusCommand axonUpdateFirmBankingRequestStatusCommand = new AxonUpdateFirmBankingRequestStatusCommand(
        command.getEventStreamId(),
        command.getStatus(),
        command.getErrorMessage(),
        command.getFirmBankingRequestId()
    );

    commandGateway.send(axonUpdateFirmBankingRequestStatusCommand)
        .whenComplete((result, throwable) -> {
          if (throwable != null) {
            log.error("axonUpdateFirmBankingRequestStatusCommand failed", throwable);
          }
        });
  }
}
