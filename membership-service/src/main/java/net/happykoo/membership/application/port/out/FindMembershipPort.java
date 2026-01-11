package net.happykoo.membership.application.port.out;

import java.util.List;
import net.happykoo.membership.application.port.in.command.FindMembershipByAddressCommand;
import net.happykoo.membership.domain.Membership;

public interface FindMembershipPort {

  Membership findMembership(Membership.MembershipId membershipId);

  List<Membership> findMembershipByAddress(Membership.MembershipAddress address);
}
