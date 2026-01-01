package net.happykoo.membership.application.port.in.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.happykoo.membership.common.SelfValidating;

@Data
@EqualsAndHashCode(callSuper = false)
public class ModifyMembershipCommand extends SelfValidating<ModifyMembershipCommand> {

  @NotNull
  @NotBlank
  private final String membershipId;

  @NotNull
  @NotBlank
  private final String name;

  @NotNull
  @NotBlank
  private final String email;

  @NotNull
  @NotBlank
  private final String address;

  private final boolean isValid;

  private final boolean isCorp;

  @Builder
  public ModifyMembershipCommand(
      String membershipId,
      String name,
      String email,
      String address,
      boolean isValid,
      boolean isCorp) {
    this.membershipId = membershipId;
    this.name = name;
    this.email = email;
    this.address = address;
    this.isValid = isValid;
    this.isCorp = isCorp;

    validateSelf();
  }
}
