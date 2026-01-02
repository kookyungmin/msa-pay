package net.happykoo.banking.application.service;

import lombok.RequiredArgsConstructor;
import net.happykoo.banking.application.port.in.RegisterBankAccountUseCase;
import net.happykoo.banking.application.port.in.command.RegisterBankAccountCommand;
import net.happykoo.banking.application.port.out.FindBankAccountPort;
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
  private final FindBankAccountPort findBankAccountPort;
  private final RequestBankAccountInfoPort requestBankAccountInfoPort;

  @Override
  public RegisteredBankAccount registerBankAccount(RegisterBankAccountCommand command) {
    //은행 계좌를 등록해야하는 서비스
    //TODO: 1. 등록된 멤버인지 확인

    //2. 이미 등록된 계좌인지 확인
    if (findBankAccountPort.existsBankAccount(
        new RegisteredBankAccount.MembershipId(command.getMembershipId()),
        new RegisteredBankAccount.BankName(command.getBankName()),
        new RegisteredBankAccount.BankAccountNumber(command.getBankAccountNumber())
    )) {
      throw new IllegalArgumentException("account already registered.");
    }

    //3. 정상 계좌인지 외부 은행에 확인 -> 정상 계좌라면 등록, 그렇지 않으면 에러 발생
    var bankAccountData = requestBankAccountInfoPort.requestBankAccountInfo(
        new BankAccountPayload(command.getBankName(), command.getBankAccountNumber())
    );
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
