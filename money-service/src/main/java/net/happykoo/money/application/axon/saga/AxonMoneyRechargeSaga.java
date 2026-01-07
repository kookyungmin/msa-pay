package net.happykoo.money.application.axon.saga;

import lombok.extern.slf4j.Slf4j;
import net.happykoo.money.application.axon.command.AxonIncreaseMemberMoneyCommand;
import net.happykoo.money.application.port.out.FindRegisteredBankAccountPort;
import net.happykoo.money.application.port.out.FirmBankingPort;
import net.happykoo.money.application.port.out.data.RegisteredBankAccountInfo;
import net.happykoo.money.application.port.out.payload.FindRegisteredBankAccountPayload;
import net.happykoo.money.application.port.out.payload.FirmBankingPayload;
import net.happykoo.money.domain.axon.event.AxonFirmBankingResultEvent;
import net.happykoo.money.domain.axon.event.AxonIncreaseMemberMoneyEvent;
import net.happykoo.money.domain.axon.event.AxonRechargeMoneyEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
@Slf4j
public class AxonMoneyRechargeSaga {

  @Autowired
  private transient CommandGateway commandGateway;

  @Autowired
  private transient FindRegisteredBankAccountPort findRegisteredBankAccountPort;

  @Autowired
  private transient FirmBankingPort firmBankingPort;

  private Long membershipId;
  private int moneyAmount;
  private String memberMoneyEventStreamId;
  private RegisteredBankAccountInfo registeredBankAccountInfo;

  @StartSaga
  @SagaEventHandler(associationProperty = "rechargingRequestId")
  public void on(AxonRechargeMoneyEvent event) {
    log.info("AxonRechargeMoneyEvent Saga Handler >>> {}", event);

    this.membershipId = event.membershipId();
    this.moneyAmount = event.moneyAmount();
    this.memberMoneyEventStreamId = event.memberMoneyEventStreamId();

    this.registeredBankAccountInfo = findRegisteredBankAccountPort.findRegisteredBankAccountInfo(
        new FindRegisteredBankAccountPayload(String.valueOf(event.membershipId()))
    );

    if (registeredBankAccountInfo == null
        || !registeredBankAccountInfo.isValid()) {
      log.error("account is not valid.");
      SagaLifecycle.end();
      return;
    }

    firmBankingPort.firmBanking(new FirmBankingPayload(
        event.rechargingRequestId(),
        registeredBankAccountInfo.getBankName(),
        registeredBankAccountInfo.getBankAccountNumber(),
        "해피해피은행",
        "1234-1234-111",
        event.moneyAmount()
    ));
  }

  @SagaEventHandler(associationProperty = "rechargingRequestId")
  public void on(AxonFirmBankingResultEvent event) {
    log.info("AxonFirmBankingResultEvent Saga Handler >>> {}", event);

    if (event.isSuccess()) {
      var axonIncreaseMemberMoneyCommand = new AxonIncreaseMemberMoneyCommand(
          memberMoneyEventStreamId,
          membershipId,
          moneyAmount,
          event.rechargingRequestId()
      );

      commandGateway.send(axonIncreaseMemberMoneyCommand)
          .whenComplete((r, ex) -> {
            if (ex != null) {
              log.error("IncreaseMoneyRequestByEvent failed", ex);
              //보상 트랜잭션 실행
              firmBankingPort.firmBanking(new FirmBankingPayload(
                  null,
                  "해피해피은행",
                  "1234-1234-111",
                  registeredBankAccountInfo.getBankName(),
                  registeredBankAccountInfo.getBankAccountNumber(),
                  moneyAmount
              ));
            }
          });
    } else {
      log.error("FirmBanking failed. {}", event);
      SagaLifecycle.end();
    }
  }

  @SagaEventHandler(associationProperty = "rechargingRequestId")
  @EndSaga
  public void on(AxonIncreaseMemberMoneyEvent event) {
    log.info("AxonIncreaseMemberMoneyEvent Saga Handler Saga End >>> {}", event);
  }
}
