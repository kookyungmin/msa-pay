package net.happykoo.banking.application.axon.saga;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import net.happykoo.banking.application.axon.command.AxonCreateFirmBankingRequestCommand;
import net.happykoo.banking.application.axon.command.AxonUpdateFirmBankingRequestStatusCommand;
import net.happykoo.banking.application.port.out.RequestBankAccountInfoPort;
import net.happykoo.banking.application.port.out.RequestFirmBankingPort;
import net.happykoo.banking.application.port.out.SendFirmBankingResultPort;
import net.happykoo.banking.application.port.out.payload.BankAccountPayload;
import net.happykoo.banking.application.port.out.payload.FirmBankingPayload;
import net.happykoo.banking.application.port.out.payload.SendFirmBankingResultPayload;
import net.happykoo.banking.domain.FirmBankingRequestStatus;
import net.happykoo.banking.domain.axon.event.AxonCreateFirmBankingRequestEvent;
import net.happykoo.banking.domain.axon.event.AxonFirmBankingRequestEvent;
import net.happykoo.banking.domain.axon.event.AxonUpdateFirmBankingRequestStatusEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
@Slf4j
public class AxonFirmBankingSaga {

  @Autowired
  private transient RequestBankAccountInfoPort requestBankAccountInfoPort;
  @Autowired
  private transient RequestFirmBankingPort requestFirmBankingPort;
  @Autowired
  private transient CommandGateway commandGateway;
  @Autowired
  private transient SendFirmBankingResultPort sendFirmBankingResultPort;

  private String externalRequestId;

  @StartSaga
  @SagaEventHandler(associationProperty = "firmBankingRequestId")
  public void on(AxonFirmBankingRequestEvent event) {
    log.info("AxonFirmBankingRequestEvent Saga Handler >>> {}", event);

    this.externalRequestId = event.externalRequestId();

    AxonCreateFirmBankingRequestCommand axonCreateFirmBankingRequestCommand = new AxonCreateFirmBankingRequestCommand(
        UUID.randomUUID().toString(),
        event.fromBankName(),
        event.fromBankAccount(),
        event.toBankName(),
        event.toBankAccount(),
        event.moneyAmount(),
        event.firmBankingRequestId()
    );

    commandGateway.send(axonCreateFirmBankingRequestCommand)
        .whenComplete((result, throwable) -> {
          if (throwable != null) {
            log.error("AxonCreateFirmBankingRequestCommand failed", throwable);
          }
        });
  }

  @SagaEventHandler(associationProperty = "firmBankingRequestId")
  public void on(AxonCreateFirmBankingRequestEvent event) {
    log.info("AxonCreateFirmBankingRequestEvent Saga Handler >>> {}", event);

    var fromBankData = requestBankAccountInfoPort.requestBankAccountInfo(
        new BankAccountPayload(event.fromBankName(), event.fromBankAccountNumber())
    );
    var toBankData = requestBankAccountInfoPort.requestBankAccountInfo(
        new BankAccountPayload(event.toBankName(), event.toBankAccountNumber())
    );

    if (!fromBankData.isValid() || !toBankData.isValid()) {
      sendErrorStatusCommand(event.aggregateId(), event.firmBankingRequestId(),
          "account is not valid.");
    } else {
      //3. 외부 은행에 펌뱅킹 요청
      var result = requestFirmBankingPort.requestFirmBanking(new FirmBankingPayload(
          event.fromBankName(),
          event.fromBankAccountNumber(),
          event.toBankName(),
          event.toBankAccountNumber(),
          event.moneyAmount()
      ));
      if (result.isSuccess()) {
        sendSuccessStatusCommand(event.aggregateId(), event.firmBankingRequestId());
      } else {
        sendErrorStatusCommand(event.aggregateId(), event.firmBankingRequestId(),
            "failed to request firm banking.");
      }
    }
  }

  @EndSaga
  @SagaEventHandler(associationProperty = "firmBankingRequestId")
  public void on(AxonUpdateFirmBankingRequestStatusEvent event) {
    log.info(
        "AxonUpdateFirmBankingRequestStatusEvent Saga ended: firmBankingRequestId ={}, externalRequestId = {}",
        event.firmBankingRequestId(), externalRequestId);
    if (externalRequestId != null) {
      sendFirmBankingResultPort.sendFirmBankingResult(new SendFirmBankingResultPayload(
          FirmBankingRequestStatus.SUCCESS.equals(event.status()),
          event.errorMessage(),
          externalRequestId
      ));

    }
  }

  private void sendSuccessStatusCommand(String aggregateId, String firmBankingRequestId) {
    var command = new AxonUpdateFirmBankingRequestStatusCommand(
        aggregateId,
        FirmBankingRequestStatus.SUCCESS,
        null,
        firmBankingRequestId
    );
    commandGateway.send(command);
  }

  private void sendErrorStatusCommand(String aggregateId, String firmBankingRequestId,
      String errorMsg) {
    var command = new AxonUpdateFirmBankingRequestStatusCommand(
        aggregateId,
        FirmBankingRequestStatus.FAILED,
        errorMsg,
        firmBankingRequestId
    );
    commandGateway.send(command);
  }
}
