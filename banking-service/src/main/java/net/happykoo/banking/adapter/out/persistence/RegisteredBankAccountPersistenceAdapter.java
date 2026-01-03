package net.happykoo.banking.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import net.happykoo.banking.adapter.out.persistence.jpa.JpaRegisteredBankAccountRepository;
import net.happykoo.banking.adapter.out.persistence.jpa.entity.JpaRegisteredBankAccountEntity;
import net.happykoo.banking.application.port.out.FindBankAccountPort;
import net.happykoo.banking.application.port.out.RegisterBankAccountPort;
import net.happykoo.banking.domain.RegisteredBankAccount;
import net.happykoo.banking.domain.RegisteredBankAccount.BankAccountNumber;
import net.happykoo.banking.domain.RegisteredBankAccount.BankName;
import net.happykoo.banking.domain.RegisteredBankAccount.MembershipId;
import net.happykoo.common.annotation.PersistenceAdapter;

@PersistenceAdapter
@RequiredArgsConstructor
public class RegisteredBankAccountPersistenceAdapter implements RegisterBankAccountPort,
    FindBankAccountPort {

  private final JpaRegisteredBankAccountRepository jpaRegisteredBankAccountRepository;
  private final RegisteredBankAccountMapper registeredBankAccountMapper;

  @Override
  public RegisteredBankAccount createBankAccount(
      MembershipId membershipId,
      BankName bankName,
      BankAccountNumber bankAccountNumber
  ) {
    var entity = jpaRegisteredBankAccountRepository.save(
        new JpaRegisteredBankAccountEntity(
            membershipId.value(),
            bankName.value(),
            bankAccountNumber.value(),
            true
        ));

    return registeredBankAccountMapper.mapToDomainEntity(entity);
  }

  @Override
  public boolean existsBankAccount(
      MembershipId membershipId,
      BankName bankName,
      BankAccountNumber bankAccountNumber
  ) {
    return jpaRegisteredBankAccountRepository.existsByMembershipIdAndBankNameAndBankAccountNumber(
        membershipId.value(),
        bankName.value(),
        bankAccountNumber.value()
    );
  }
}
