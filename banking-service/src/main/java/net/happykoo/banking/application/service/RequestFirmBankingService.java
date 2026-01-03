package net.happykoo.banking.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.happykoo.banking.application.port.in.RequestFirmBankingUseCase;
import net.happykoo.banking.application.port.in.command.RequestFirmBankingCommand;
import net.happykoo.banking.application.port.out.RequestBankAccountInfoPort;
import net.happykoo.banking.application.port.out.RequestFirmBankingPort;
import net.happykoo.banking.application.port.out.SaveFirmBankingRequestPort;
import net.happykoo.banking.application.port.out.payload.BankAccountPayload;
import net.happykoo.banking.application.port.out.payload.FirmBankingPayload;
import net.happykoo.banking.domain.FirmBankingRequest;
import net.happykoo.banking.domain.FirmBankingRequest.FirmBankingRequestId;
import net.happykoo.banking.domain.FirmBankingRequest.FirmBankingStatus;
import net.happykoo.banking.domain.FirmBankingRequest.FromBankAccountNumber;
import net.happykoo.banking.domain.FirmBankingRequest.FromBankName;
import net.happykoo.banking.domain.FirmBankingRequest.MoneyAmount;
import net.happykoo.banking.domain.FirmBankingRequest.ToBankAccountNumber;
import net.happykoo.banking.domain.FirmBankingRequest.ToBankName;
import net.happykoo.common.annotation.UseCase;

@UseCase
@RequiredArgsConstructor
public class RequestFirmBankingService implements RequestFirmBankingUseCase {

  private final SaveFirmBankingRequestPort saveFirmBankingRequestPort;
  private final RequestBankAccountInfoPort requestBankAccountInfoPort;
  private final RequestFirmBankingPort requestFirmBankingPort;

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
        new FirmBankingStatus(firmBankingRequest.getFirmBankingStatus())
    );
  }
}
