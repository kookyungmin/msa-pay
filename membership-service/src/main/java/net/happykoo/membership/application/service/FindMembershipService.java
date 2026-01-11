package net.happykoo.membership.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.happykoo.common.annotation.UseCase;
import net.happykoo.membership.application.port.in.FindMembershipUseCase;
import net.happykoo.membership.application.port.in.command.FindMembershipByAddressCommand;
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

  @Override
  public List<Membership> findMembershipsByAddress(FindMembershipByAddressCommand command) {
    return findMembershipPort.findMembershipByAddress(
        new Membership.MembershipAddress(command.getAddress())
    );
  }
}
