package net.happykoo.membership.application.service;

import lombok.RequiredArgsConstructor;
import net.happykoo.common.UseCase;
import net.happykoo.membership.application.port.in.FindMembershipUseCase;
import net.happykoo.membership.application.port.in.command.FindMembershipCommand;
import net.happykoo.membership.application.port.out.FindMembershipPort;
import net.happykoo.membership.domain.Membership;
import net.happykoo.membership.domain.Membership.MembershipId;

@UseCase
@RequiredArgsConstructor
public class FindMembershipService implements FindMembershipUseCase {

  private final FindMembershipPort findMembershipPort;

  @Override
  public Membership findMembership(FindMembershipCommand command) {
    return findMembershipPort.findMembership(new MembershipId(command.getMembershipId()));
  }
}
