package net.happykoo.banking.application.port.in.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.happykoo.common.validation.SelfValidating;

@Data
@EqualsAndHashCode(callSuper = false)
public class RegisterBankAccountCommand extends SelfValidating<RegisterBankAccountCommand> {

  @NotNull
  @NotBlank
  private String membershipId;

  @NotNull
  @NotBlank
  private String bankName;

  @NotNull
  @NotBlank
  private String bankAccountNumber;

  @Builder
  public RegisterBankAccountCommand(
      String membershipId,
      String bankName,
      String bankAccountNumber
  ) {
    this.membershipId = membershipId;
    this.bankName = bankName;
    this.bankAccountNumber = bankAccountNumber;

    validateSelf();
  }
}
