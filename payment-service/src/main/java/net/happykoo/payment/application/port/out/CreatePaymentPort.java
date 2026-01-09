package net.happykoo.payment.application.port.out;

import net.happykoo.payment.domain.Payment;

public interface CreatePaymentPort {

  Payment createPayment(
      Payment.MembershipId membershipId,
      Payment.Price price,
      Payment.FranchiseId franchiseId,
      Payment.FranchiseFeeRate franchiseFeeRate
  );
}
