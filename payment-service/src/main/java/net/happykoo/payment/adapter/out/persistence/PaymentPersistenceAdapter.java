package net.happykoo.payment.adapter.out.persistence;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import net.happykoo.common.annotation.PersistenceAdapter;
import net.happykoo.payment.adapter.out.persistence.jpa.JpaPaymentRepository;
import net.happykoo.payment.adapter.out.persistence.jpa.entity.JpaPaymentEntity;
import net.happykoo.payment.application.port.out.CreatePaymentPort;
import net.happykoo.payment.domain.Payment;
import net.happykoo.payment.domain.Payment.FranchiseFeeRate;
import net.happykoo.payment.domain.Payment.FranchiseId;
import net.happykoo.payment.domain.Payment.MembershipId;
import net.happykoo.payment.domain.Payment.Price;
import net.happykoo.payment.domain.PaymentStatus;

@PersistenceAdapter
@RequiredArgsConstructor
public class PaymentPersistenceAdapter implements CreatePaymentPort {

  private final JpaPaymentRepository jpaPaymentRepository;
  private final PaymentMapper paymentMapper;

  @Override
  public Payment createPayment(
      MembershipId membershipId,
      Price price,
      FranchiseId franchiseId,
      FranchiseFeeRate franchiseFeeRate
  ) {
    var entity = new JpaPaymentEntity(
        membershipId.value(),
        price.value(),
        franchiseId.value(),
        franchiseFeeRate.value(),
        PaymentStatus.REQUESTED,
        LocalDateTime.now()
    );

    return paymentMapper.mapToDomainEntity(jpaPaymentRepository.save(entity));
  }
}
