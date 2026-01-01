package net.happykoo.membership.application.port.out;

import net.happykoo.membership.domain.Membership;

public interface ModifyMembershipPort {

  Membership updateMembership(
      Membership.MembershipId membershipId,
      Membership.MembershipName membershipName,
      Membership.MembershipEmail membershipEmail,
      Membership.MembershipAddress membershipAddress,
      Membership.MembershipIsValid membershipIsValid,
      Membership.MembershipIsCorp membershipIsCorp
  );

}
