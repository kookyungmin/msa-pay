package net.happykoo.money.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import net.happykoo.common.annotation.PersistenceAdapter;
import net.happykoo.money.adapter.out.persistence.jpa.JpaMemberMoneyRepository;
import net.happykoo.money.adapter.out.persistence.jpa.entity.JpaMemberMoneyEntity;
import net.happykoo.money.application.port.out.ChangeMemberMoneyPort;
import net.happykoo.money.domain.MemberMoney.Balance;
import net.happykoo.money.domain.MemberMoney.MembershipId;

@PersistenceAdapter
@RequiredArgsConstructor
public class MemberMoneyPersistenceAdapter implements ChangeMemberMoneyPort {

  private final JpaMemberMoneyRepository jpaMemberMoneyRepository;

  @Override
  public void increaseMemberMoney(MembershipId membershipId, Balance balance) {
    var entity = jpaMemberMoneyRepository.findByMembershipId(membershipId.value())
        .orElse(new JpaMemberMoneyEntity(membershipId.value(), 0));

    entity.setBalance(entity.getBalance() + balance.value());

    jpaMemberMoneyRepository.save(entity);
  }
}
