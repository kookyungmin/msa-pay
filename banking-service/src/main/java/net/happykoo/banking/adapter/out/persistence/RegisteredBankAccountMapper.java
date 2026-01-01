package net.happykoo.banking.adapter.out.persistence;

import net.happykoo.banking.adapter.out.persistence.jpa.entity.JpaRegisteredBankAccountEntity;
import net.happykoo.banking.domain.RegisteredBankAccount;
import org.springframework.stereotype.Component;

@Component
public class RegisteredBankAccountMapper {

  RegisteredBankAccount mapToDomainEntity(JpaRegisteredBankAccountEntity entity) {
    return RegisteredBankAccount.generateRegisteredBankAccount(
        new RegisteredBankAccount.RegisteredBankAccountId(entity.getId().toString()),
        new RegisteredBankAccount.MembershipId(entity.getMembershipId()),
        new RegisteredBankAccount.BankName(entity.getBankName()),
        new RegisteredBankAccount.BankAccountNumber(entity.getBankAccountNumber()),
        new RegisteredBankAccount.LinkedStatusIsValid(entity.isValid())
    );
  }
}
