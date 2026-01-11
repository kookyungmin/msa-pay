package net.happykoo.membership.adapter.out.persistence;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.happykoo.common.annotation.PersistenceAdapter;
import net.happykoo.membership.adapter.out.persistence.jpa.JpaMembershipRepository;
import net.happykoo.membership.adapter.out.persistence.jpa.entity.JpaMembershipEntity;
import net.happykoo.membership.application.port.in.command.FindMembershipByAddressCommand;
import net.happykoo.membership.application.port.out.FindMembershipPort;
import net.happykoo.membership.application.port.out.ModifyMembershipPort;
import net.happykoo.membership.application.port.out.RegisterMembershipPort;
import net.happykoo.membership.domain.Membership;
import net.happykoo.membership.domain.Membership.MembershipAddress;
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
    var entity = jpaMembershipRepository.save(new JpaMembershipEntity(
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
  public List<Membership> findMembershipByAddress(MembershipAddress address) {
    return jpaMembershipRepository.findByAddress(address.value())
        .stream()
        .map(membershipMapper::mapToDomainEntity)
        .toList();
  }

  @Override
  public Membership updateMembership(
      Membership.MembershipId membershipId,
      Membership.MembershipName membershipName,
      Membership.MembershipEmail membershipEmail,
      Membership.MembershipAddress membershipAddress,
      Membership.MembershipIsValid membershipIsValid,
      Membership.MembershipIsCorp membershipIsCorp) {

    var entity = jpaMembershipRepository
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
