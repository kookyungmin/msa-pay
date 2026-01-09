package net.happykoo.payment.adapter.out.persistence;


import net.happykoo.payment.adapter.out.persistence.jpa.entity.JpaPaymentEntity;
import net.happykoo.payment.domain.Payment;
import net.happykoo.payment.domain.Payment.FranchiseFeeRate;
import net.happykoo.payment.domain.Payment.FranchiseId;
import net.happykoo.payment.domain.Payment.MembershipId;
import net.happykoo.payment.domain.Payment.PaymentId;
import net.happykoo.payment.domain.Payment.Price;
import net.happykoo.payment.domain.Payment.Status;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

  Payment mapToDomainEntity(JpaPaymentEntity entity) {
    return new Payment(
        new PaymentId(entity.getId().toString()),
        new MembershipId(entity.getMembershipId()),
        new Price(entity.getPrice()),
        new FranchiseId(entity.getFranchiseId()),
        new FranchiseFeeRate(entity.getFranchiseFeeRate()),
        new Status(entity.getStatus()),
        new Payment.ApprovedAt(entity.getApprovedAt())
    );
  }
}
