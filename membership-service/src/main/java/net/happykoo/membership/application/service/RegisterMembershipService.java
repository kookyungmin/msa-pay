package net.happykoo.membership.application.service;

import lombok.RequiredArgsConstructor;
import net.happykoo.common.annotation.UseCase;
import net.happykoo.membership.application.port.in.RegisterMembershipUseCase;
import net.happykoo.membership.application.port.in.command.RegisterMembershipCommand;
import net.happykoo.membership.application.port.out.RegisterMembershipPort;
import net.happykoo.membership.domain.Membership;
import net.happykoo.membership.domain.Membership.MembershipAddress;
import net.happykoo.membership.domain.Membership.MembershipEmail;
import net.happykoo.membership.domain.Membership.MembershipIsCorp;
import net.happykoo.membership.domain.Membership.MembershipName;

@UseCase
@RequiredArgsConstructor
public class RegisterMembershipService implements RegisterMembershipUseCase {

  private final RegisterMembershipPort registerMembershipPort;

  @Override
  public Membership registerMembership(RegisterMembershipCommand command) {
    return registerMembershipPort.createMembership(
        new MembershipName(command.getName()),
        new MembershipEmail(command.getEmail()),
        new MembershipAddress(command.getAddress()),
        new MembershipIsCorp(command.isCorp())
    );
  }
}
