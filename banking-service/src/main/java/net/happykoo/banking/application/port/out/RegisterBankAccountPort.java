package net.happykoo.banking.application.port.out;


import net.happykoo.banking.domain.RegisteredBankAccount;

public interface RegisterBankAccountPort {

  RegisteredBankAccount createBankAccount(
      RegisteredBankAccount.MembershipId membershipId,
      RegisteredBankAccount.BankName bankName,
      RegisteredBankAccount.BankAccountNumber bankAccountNumber
  );

}
