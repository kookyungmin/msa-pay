package net.happykoo.money.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import net.happykoo.common.annotation.PersistenceAdapter;
import net.happykoo.money.adapter.out.persistence.jpa.JpaMoneyChangingRequestRepository;
import net.happykoo.money.adapter.out.persistence.jpa.entity.JpaMoneyChangingRequestEntity;
import net.happykoo.money.application.port.out.SaveMoneyChangingRequestPort;
import net.happykoo.money.domain.MoneyChangingRequest;
import net.happykoo.money.domain.MoneyChangingRequest.MoneyAmount;
import net.happykoo.money.domain.MoneyChangingRequest.MoneyChangingRequestId;
import net.happykoo.money.domain.MoneyChangingRequest.RequestStatus;
import net.happykoo.money.domain.MoneyChangingRequest.RequestType;
import net.happykoo.money.domain.MoneyChangingRequest.TargetMembershipId;
import net.happykoo.money.domain.MoneyChangingRequestStatus;

@PersistenceAdapter
@RequiredArgsConstructor
public class MoneyChangingRequestPersistenceAdapter implements SaveMoneyChangingRequestPort {

  private final JpaMoneyChangingRequestRepository jpaMoneyChangingRequestRepository;
  private final MoneyChangingRequestMapper moneyChangingRequestMapper;

  @Override
  public MoneyChangingRequest createMoneyChangingRequest(
      TargetMembershipId targetMembershipId,
      RequestType requestType,
      MoneyAmount moneyAmount
  ) {
    var entity = new JpaMoneyChangingRequestEntity(
        targetMembershipId.value(),
        requestType.value(),
        moneyAmount.value(),
        MoneyChangingRequestStatus.REQUESTED,
        null
    );
    return moneyChangingRequestMapper.mapToDomainEntity(
        jpaMoneyChangingRequestRepository.save(entity)
    );
  }

  @Override
  public MoneyChangingRequest updateMoneyChangingStatus(
      MoneyChangingRequestId moneyChangingRequestId,
      RequestStatus requestStatus
  ) {
    var entity = jpaMoneyChangingRequestRepository.findById(
            Long.parseLong(moneyChangingRequestId.value()))
        .orElseThrow(() -> new IllegalArgumentException(
            "entity does not exists : " + moneyChangingRequestId.value()));
    entity.setStatus(requestStatus.value());

    return moneyChangingRequestMapper.mapToDomainEntity(
        jpaMoneyChangingRequestRepository.save(entity));
  }
}
