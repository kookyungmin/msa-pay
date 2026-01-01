package net.happykoo.banking.application.service;

import lombok.RequiredArgsConstructor;
import net.happykoo.banking.application.port.in.RegisterBankAccountUseCase;
import net.happykoo.banking.application.port.in.command.RegisterBankAccountCommand;
import net.happykoo.banking.application.port.out.RegisterBankAccountPort;
import net.happykoo.banking.application.port.out.RequestBankAccountInfoPort;
import net.happykoo.banking.application.port.out.payload.BankAccountPayload;
import net.happykoo.banking.application.port.out.data.BankAccountData;
import net.happykoo.banking.domain.RegisteredBankAccount;
import net.happykoo.common.UseCase;

@UseCase
@RequiredArgsConstructor
public class RegisterBankAccountService implements RegisterBankAccountUseCase {

  private final RegisterBankAccountPort registerBankAccountPort;
  private final RequestBankAccountInfoPort requestBankAccountInfoPort;

  @Override
  public RegisteredBankAccount registerBankAccount(RegisterBankAccountCommand command) {
    //은행 계좌를 등록해야하는 서비스
    //TODO: 1. 등록된 멤버인지 확인

    //2. 등록된 계좌인지 확인 (외부 은행에 이 계좌가 정상인지 확인)
    BankAccountData bankAccountData = requestBankAccountInfoPort.requestBankAccountInfo(
        new BankAccountPayload(command.getBankName(), command.getBankAccountNumber())
    );

    //3. 등록 가능한 계좌라면 등록, 등록 가능하지 않으면 에러
    if (!bankAccountData.isValid()) {
      throw new IllegalArgumentException("account is not valid.");
    }

    return registerBankAccountPort.createBankAccount(
        new RegisteredBankAccount.MembershipId(command.getMembershipId()),
        new RegisteredBankAccount.BankName(command.getBankName()),
        new RegisteredBankAccount.BankAccountNumber(command.getBankAccountNumber())
    );
  }
}
