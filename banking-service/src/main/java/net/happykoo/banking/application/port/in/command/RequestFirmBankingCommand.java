package net.happykoo.banking.application.port.in.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.happykoo.common.validation.SelfValidating;

@Data
@EqualsAndHashCode(callSuper = false)
public class RequestFirmBankingCommand extends SelfValidating<RequestFirmBankingCommand> {

  @NotNull
  @NotBlank
  private String fromBankName;

  @NotNull
  @NotBlank
  private String fromBankAccountNumber;

  @NotNull
  @NotBlank
  private String toBankName;

  @NotNull
  @NotBlank
  private String toBankAccountNumber;

  private int moneyAmount;

  @Builder
  public RequestFirmBankingCommand(
      String fromBankName,
      String fromBankAccountNumber,
      String toBankName,
      String toBankAccountNumber,
      int moneyAmount
  ) {
    this.fromBankName = fromBankName;
    this.fromBankAccountNumber = fromBankAccountNumber;
    this.toBankName = toBankName;
    this.toBankAccountNumber = toBankAccountNumber;
    this.moneyAmount = moneyAmount;

    validateSelf();
  }
}
