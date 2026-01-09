package net.happykoo.payment.application.port.in.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import net.happykoo.common.validation.SelfValidating;


@Getter
public class RequestPaymentCommand extends SelfValidating<RequestPaymentCommand> {

  @NotNull
  @NotBlank
  private final String requestMembershipId;

  private final int requestPrice;

  @NotNull
  @NotBlank
  private final String franchiseId;

  @NotNull
  @NotBlank
  private final String franchiseFeeRate;

  @Builder
  public RequestPaymentCommand(
      String requestMembershipId,
      int requestPrice,
      String franchiseId,
      String franchiseFeeRate
  ) {
    this.requestMembershipId = requestMembershipId;
    this.requestPrice = requestPrice;
    this.franchiseId = franchiseId;
    this.franchiseFeeRate = franchiseFeeRate;

    validateSelf();
  }
}
