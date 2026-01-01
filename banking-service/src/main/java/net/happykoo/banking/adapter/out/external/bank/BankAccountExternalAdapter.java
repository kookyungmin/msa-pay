package net.happykoo.banking.adapter.out.external.bank;

import net.happykoo.banking.application.port.out.RequestBankAccountInfoPort;
import net.happykoo.banking.application.port.out.payload.BankAccountPayload;
import net.happykoo.banking.application.port.out.data.BankAccountData;
import net.happykoo.common.ExternalSystemAdapter;

@ExternalSystemAdapter
public class BankAccountExternalAdapter implements RequestBankAccountInfoPort {

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
}
