package net.happykoo.payment.adapter.out.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.happykoo.payment.domain.PaymentStatus;

@Entity
@Table(name = "payment")
@NoArgsConstructor
@Getter
public class JpaPaymentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String membershipId;

  private int price;

  private String franchiseId;

  private String franchiseFeeRate;

  @Enumerated(value = EnumType.STRING)
  private PaymentStatus status;

  private LocalDateTime approvedAt;

  public JpaPaymentEntity(
      Long id,
      String membershipId,
      int price,
      String franchiseId,
      String franchiseFeeRate,
      PaymentStatus status,
      LocalDateTime approvedAt
  ) {
    this.id = id;
    this.membershipId = membershipId;
    this.price = price;
    this.franchiseId = franchiseId;
    this.franchiseFeeRate = franchiseFeeRate;
    this.status = status;
    this.approvedAt = approvedAt;
  }

  public JpaPaymentEntity(
      String membershipId,
      int price,
      String franchiseId,
      String franchiseFeeRate,
      PaymentStatus status,
      LocalDateTime approvedAt
  ) {
    this(null, membershipId, price, franchiseId, franchiseFeeRate, status, approvedAt);
  }
}
