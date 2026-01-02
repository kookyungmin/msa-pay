package net.happykoo.banking.adapter.out.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.happykoo.banking.domain.FirmBankingRequestStatus;

@Entity
@Table(name = "firm_banking_request")
@Data
@NoArgsConstructor
public class JpaFirmBankingRequestEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String fromBankName;
  private String fromBankAccountNumber;
  private String toBankName;
  private String toBankAccountNumber;
  private int moneyAmount;
  @Enumerated(value = EnumType.STRING)
  private FirmBankingRequestStatus status;
  private String errorMsg;

  public JpaFirmBankingRequestEntity(
      Long id,
      String fromBankName,
      String fromBankAccountNumber,
      String toBankName,
      String toBankAccountNumber,
      int moneyAmount,
      FirmBankingRequestStatus status,
      String errorMsg
  ) {
    this.id = id;
    this.fromBankName = fromBankName;
    this.fromBankAccountNumber = fromBankAccountNumber;
    this.toBankName = toBankName;
    this.toBankAccountNumber = toBankAccountNumber;
    this.moneyAmount = moneyAmount;
    this.status = status;
    this.errorMsg = errorMsg;
  }

  public JpaFirmBankingRequestEntity(
      String fromBankName,
      String fromBankAccountNumber,
      String toBankName,
      String toBankAccountNumber,
      int moneyAmount,
      FirmBankingRequestStatus status,
      String errorMsg
  ) {
    this(null,
        fromBankName,
        fromBankAccountNumber,
        toBankName,
        toBankAccountNumber,
        moneyAmount,
        status,
        errorMsg);
  }
}
