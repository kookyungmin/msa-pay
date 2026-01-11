package net.happykoo.membership.application.port.in.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.happykoo.common.validation.SelfValidating;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class FindMembershipByAddressCommand extends SelfValidating<FindMembershipByAddressCommand> {

  @NotNull
  @NotBlank
  private final String address;

}
