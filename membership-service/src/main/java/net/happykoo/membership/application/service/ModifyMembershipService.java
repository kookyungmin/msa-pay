package net.happykoo.membership.application.service;

import lombok.RequiredArgsConstructor;
import net.happykoo.common.annotation.UseCase;
import net.happykoo.membership.application.port.in.ModifyMembershipUseCase;
import net.happykoo.membership.application.port.in.command.ModifyMembershipCommand;
import net.happykoo.membership.application.port.out.ModifyMembershipPort;
import net.happykoo.membership.domain.Membership;

@UseCase
@RequiredArgsConstructor
public class ModifyMembershipService implements ModifyMembershipUseCase {

  private final ModifyMembershipPort modifyMembershipPort;

  @Override
  public Membership modifyMembership(ModifyMembershipCommand command) {
    return modifyMembershipPort.updateMembership(
        new Membership.MembershipId(command.getMembershipId()),
        new Membership.MembershipName(command.getName()),
        new Membership.MembershipEmail(command.getEmail()),
        new Membership.MembershipAddress(command.getAddress()),
        new Membership.MembershipIsValid(command.isValid()),
        new Membership.MembershipIsCorp(command.isCorp())
    );
  }
}
