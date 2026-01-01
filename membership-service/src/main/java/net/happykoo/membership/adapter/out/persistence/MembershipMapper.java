package net.happykoo.membership.adapter.out.persistence;

import net.happykoo.membership.adapter.out.persistence.jpa.entity.MembershipJpaEntity;
import net.happykoo.membership.domain.Membership;
import org.springframework.stereotype.Component;

@Component
public class MembershipMapper {

  Membership mapToDomainEntity(MembershipJpaEntity entity) {
    return Membership.generateMember(
        new Membership.MembershipId(entity.getId().toString()),
        new Membership.MembershipName(entity.getName()),
        new Membership.MembershipEmail(entity.getEmail()),
        new Membership.MembershipAddress(entity.getAddress()),
        new Membership.MembershipIsValid(entity.isValid()),
        new Membership.MembershipIsCorp(entity.isCorp())
    );
  }

}
