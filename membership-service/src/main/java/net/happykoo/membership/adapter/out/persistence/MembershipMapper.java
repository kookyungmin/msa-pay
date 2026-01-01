package net.happykoo.membership.adapter.out.persistence;

import net.happykoo.membership.adapter.out.persistence.jpa.entity.MembershipJpaEntity;
import net.happykoo.membership.domain.Membership;
import org.springframework.stereotype.Component;

@Component
public class MembershipMapper {

  Membership mapToDomainEntity(MembershipJpaEntity membership) {
    return Membership.generateMember(
        new Membership.MembershipId(membership.getId().toString()),
        new Membership.MembershipName(membership.getName()),
        new Membership.MembershipEmail(membership.getEmail()),
        new Membership.MembershipAddress(membership.getAddress()),
        new Membership.MembershipIsValid(membership.isValid()),
        new Membership.MembershipIsCorp(membership.isCorp())
    );
  }

}
