package net.happykoo.membership.application.port.in.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.happykoo.common.SelfValidating;

@Data
@EqualsAndHashCode(callSuper = false)
public class RegisterMembershipCommand extends SelfValidating<RegisterMembershipCommand> {

  @NotNull
  @NotBlank
  private final String name;

  @NotNull
  @NotBlank
  private final String email;

  @NotNull
  @NotBlank
  private final String address;

  private final boolean isCorp;

  @Builder
  public RegisterMembershipCommand(
      String name,
      String email,
      String address,
      boolean isCorp
  ) {
    this.name = name;
    this.email = email;
    this.address = address;
    this.isCorp = isCorp;

    validateSelf();
  }
}
