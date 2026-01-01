package net.happykoo.membership.adapter.out.persistence;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.happykoo.membership.adapter.out.persistence.jpa.JpaMembershipRepository;
import net.happykoo.membership.adapter.out.persistence.jpa.entity.MembershipJpaEntity;
import net.happykoo.membership.application.port.out.FindMembershipPort;
import net.happykoo.membership.application.port.out.ModifyMembershipPort;
import net.happykoo.membership.application.port.out.RegisterMembershipPort;
import net.happykoo.membership.common.PersistenceAdapter;
import net.happykoo.membership.domain.Membership;
import net.happykoo.membership.domain.Membership.MembershipId;

@PersistenceAdapter
@RequiredArgsConstructor
public class MembershipPersistenceAdapter implements RegisterMembershipPort, FindMembershipPort,
    ModifyMembershipPort {

  private final JpaMembershipRepository jpaMembershipRepository;
  private final MembershipMapper membershipMapper;

  @Override
  public Membership createMembership(
      Membership.MembershipName membershipName,
      Membership.MembershipEmail membershipEmail,
      Membership.MembershipAddress membershipAddress,
      Membership.MembershipIsCorp membershipIsCorp
  ) {
    MembershipJpaEntity entity = jpaMembershipRepository.save(new MembershipJpaEntity(
        membershipName.value(),
        membershipEmail.value(),
        membershipAddress.value(),
        true,
        membershipIsCorp.value()
    ));

    return membershipMapper.mapToDomainEntity(entity);
  }

  @Override
  public Membership findMembership(MembershipId membershipId) {
    return jpaMembershipRepository.findById(Long.parseLong(membershipId.value()))
        .map(membershipMapper::mapToDomainEntity)
        .orElseThrow(
            () -> new EntityNotFoundException("member does not exist : " + membershipId.value()));
  }

  @Override
  public Membership updateMembership(
      Membership.MembershipId membershipId,
      Membership.MembershipName membershipName,
      Membership.MembershipEmail membershipEmail,
      Membership.MembershipAddress membershipAddress,
      Membership.MembershipIsValid membershipIsValid,
      Membership.MembershipIsCorp membershipIsCorp) {

    MembershipJpaEntity entity = jpaMembershipRepository
        .findById(Long.parseLong(membershipId.value()))
        .orElseThrow(
            () -> new EntityNotFoundException("member does not exist : " + membershipId.value()));

    entity.setName(membershipName.value());
    entity.setEmail(membershipEmail.value());
    entity.setAddress(membershipAddress.value());
    entity.setValid(membershipIsValid.value());
    entity.setCorp(membershipIsCorp.value());

    return membershipMapper.mapToDomainEntity(jpaMembershipRepository.save(entity));
  }
}
