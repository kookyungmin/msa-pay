package net.happykoo.payment.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Payment {

  private final String paymentId;
  private final String membershipId;
  private final int price;
  private final String franchiseId;
  private final String franchiseFeeRate;
  private final PaymentStatus status;
  private final LocalDateTime approvedAt;

  public Payment(
      PaymentId paymentId,
      MembershipId membershipId,
      Price price,
      FranchiseId franchiseId,
      FranchiseFeeRate franchiseFeeRate,
      Status status,
      ApprovedAt approvedAt
  ) {
    this.paymentId = paymentId.value();
    this.membershipId = membershipId.value();
    this.price = price.value();
    this.franchiseId = franchiseId.value();
    this.franchiseFeeRate = franchiseFeeRate.value();
    this.status = status.value();
    this.approvedAt = approvedAt.value();

  }

  public record PaymentId(String value) {

  }

  public record MembershipId(String value) {

  }

  public record Price(int value) {

  }

  public record FranchiseId(String value) {

  }

  public record FranchiseFeeRate(String value) {

  }

  public record Status(PaymentStatus value) {

  }

  public record ApprovedAt(LocalDateTime value) {

  }
}
