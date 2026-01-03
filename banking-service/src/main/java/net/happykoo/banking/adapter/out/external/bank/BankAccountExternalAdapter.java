package net.happykoo.banking.adapter.out.external.bank;

import net.happykoo.banking.application.port.out.RequestBankAccountInfoPort;
import net.happykoo.banking.application.port.out.RequestFirmBankingPort;
import net.happykoo.banking.application.port.out.data.FirmBankingData;
import net.happykoo.banking.application.port.out.payload.BankAccountPayload;
import net.happykoo.banking.application.port.out.data.BankAccountData;
import net.happykoo.banking.application.port.out.payload.FirmBankingPayload;
import net.happykoo.common.annotation.ExternalSystemAdapter;

@ExternalSystemAdapter
public class BankAccountExternalAdapter implements RequestBankAccountInfoPort,
    RequestFirmBankingPort {

  @Override
  public BankAccountData requestBankAccountInfo(BankAccountPayload payload) {
    //TODO: 실제 외부 은행 API 를 호출해야 함
    return new BankAccountData(
        payload.bankName(),
        "TEST",
        payload.bankAccountNumber(),
        "홍길동",
        true
    );
  }

  @Override
  public FirmBankingData requestFirmBanking(FirmBankingPayload payload) {
    return new FirmBankingData(true);
  }
}
