package net.happykoo.money.adapter.out.persistence;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.happykoo.common.annotation.PersistenceAdapter;
import net.happykoo.money.adapter.out.persistence.jpa.JpaMemberMoneyRepository;
import net.happykoo.money.adapter.out.persistence.jpa.entity.JpaMemberMoneyEntity;
import net.happykoo.money.application.port.out.ChangeMemberMoneyPort;
import net.happykoo.money.application.port.out.FindMemberMoneyPort;
import net.happykoo.money.domain.MemberMoney;
import net.happykoo.money.domain.MemberMoney.Balance;
import net.happykoo.money.domain.MemberMoney.EventStreamId;
import net.happykoo.money.domain.MemberMoney.MembershipId;

@PersistenceAdapter
@RequiredArgsConstructor
public class MemberMoneyPersistenceAdapter implements ChangeMemberMoneyPort, FindMemberMoneyPort {

  private final JpaMemberMoneyRepository jpaMemberMoneyRepository;
  private final MemberMoneyMapper memberMoneyMapper;

  @Override
  public void increaseMemberMoney(MembershipId membershipId, Balance balance) {
    var entity = jpaMemberMoneyRepository.findByMembershipId(membershipId.value())
        .orElseThrow(
            () -> new EntityNotFoundException("entity does not exist : " + membershipId.value()));

    entity.setBalance(entity.getBalance() + balance.value());

    jpaMemberMoneyRepository.save(entity);
  }

  @Override
  public MemberMoney findMemberMoneyByMembershipId(MembershipId membershipId) {
    var entity = jpaMemberMoneyRepository.findByMembershipId(membershipId.value())
        .orElseThrow(
            () -> new EntityNotFoundException("entity does not exist : " + membershipId.value()));
    return memberMoneyMapper.mapToDomainEntity(entity);
  }

  @Override
  public boolean existsMemberMoneyByMembershipId(MembershipId membershipId) {
    return jpaMemberMoneyRepository.existsByMembershipId(membershipId.value());
  }

  @Override
  public List<MemberMoney> findAllMemberMoneyByMembershipIds(List<String> membershipIds) {
    return jpaMemberMoneyRepository.findAllByMembershipIdIn(membershipIds)
        .stream()
        .map(memberMoneyMapper::mapToDomainEntity)
        .toList();
  }
}
