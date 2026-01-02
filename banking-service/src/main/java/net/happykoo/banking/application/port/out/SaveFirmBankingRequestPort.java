package net.happykoo.banking.application.port.out;

import net.happykoo.banking.domain.FirmBankingRequest;

public interface SaveFirmBankingRequestPort {

  FirmBankingRequest createFirmBankingRequest(
      FirmBankingRequest.FromBankName fromBankName,
      FirmBankingRequest.FromBankAccountNumber fromBankAccountNumber,
      FirmBankingRequest.ToBankName toBankName,
      FirmBankingRequest.ToBankAccountNumber toBankAccountNumber,
      FirmBankingRequest.MoneyAmount moneyAmount
  );

  FirmBankingRequest updateFirmBankingStatus(
      FirmBankingRequest.FirmBankingRequestId firmBankingRequestId,
      FirmBankingRequest.FirmBankingStatus firmBankingStatus
  );
}
