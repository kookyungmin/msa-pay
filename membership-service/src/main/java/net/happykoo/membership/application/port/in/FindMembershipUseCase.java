package net.happykoo.membership.application.port.in;

import java.util.List;
import net.happykoo.membership.application.port.in.command.FindMembershipByAddressCommand;
import net.happykoo.membership.application.port.in.command.FindMembershipCommand;
import net.happykoo.membership.domain.Membership;

public interface FindMembershipUseCase {

  Membership findMembership(FindMembershipCommand command);

  List<Membership> findMembershipsByAddress(FindMembershipByAddressCommand command);
}
