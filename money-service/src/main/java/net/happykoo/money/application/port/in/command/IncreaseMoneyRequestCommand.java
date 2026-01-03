package net.happykoo.money.application.port.in.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import net.happykoo.common.validation.SelfValidating;

@Getter
public class IncreaseMoneyRequestCommand extends SelfValidating<IncreaseMoneyRequestCommand> {

  @NotNull
  @NotBlank
  private final String targetMembershipId;
  private final int moneyAmount;

  public IncreaseMoneyRequestCommand(
      String targetMembershipId,
      int moneyAmount
  ) {
    this.targetMembershipId = targetMembershipId;
    this.moneyAmount = moneyAmount;
    validateSelf();
  }
}
