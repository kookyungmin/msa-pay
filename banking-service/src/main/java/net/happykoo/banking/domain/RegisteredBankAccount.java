package net.happykoo.banking.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RegisteredBankAccount {

  private final String registeredBankAccountId;
  private final String membershipId;
  private final String bankName;
  private final String bankAccountNumber;
  private final boolean linkedStatusIsValid;

  public static RegisteredBankAccount generateRegisteredBankAccount(
      RegisteredBankAccountId registeredBankAccountId,
      MembershipId membershipId,
      BankName bankName,
      BankAccountNumber bankAccountNumber,
      LinkedStatusIsValid linkedStatusIsValid
  ) {
    return new RegisteredBankAccount(
        registeredBankAccountId.value(),
        membershipId.value(),
        bankName.value(),
        bankAccountNumber.value(),
        linkedStatusIsValid.value()
    );
  }

  public record RegisteredBankAccountId(String value) {

  }

  public record MembershipId(String value) {

  }

  public record BankName(String value) {

  }

  public record BankAccountNumber(String value) {

  }

  public record LinkedStatusIsValid(boolean value) {

  }
}
