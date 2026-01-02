package net.happykoo.banking.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class FirmBankingRequest {

  private final String firmBankingRequestId;

  private final String fromBankName;

  private final String fromBankAccountNumber;

  private final String toBankName;

  private final String toBankAccountNumber;

  private final int moneyAmount; //only won

  private FirmBankingRequestStatus firmBankingStatus;
  private String message;

  public void failed(String message) {
    this.firmBankingStatus = FirmBankingRequestStatus.FAILED;
    this.message = message;
  }

  public static FirmBankingRequest generateFirmBankingRequest(
      FirmBankingRequestId firmBankingRequestId,
      FromBankName fromBankName,
      FromBankAccountNumber fromBankAccountNumber,
      ToBankName toBankName,
      ToBankAccountNumber toBankAccountNumber,
      MoneyAmount moneyAmount,
      FirmBankingStatus firmBankingStatus,
      Message message
  ) {
    return new FirmBankingRequest(
        firmBankingRequestId.value(),
        fromBankName.value(),
        fromBankAccountNumber.value(),
        toBankName.value(),
        toBankAccountNumber.value(),
        moneyAmount.value(),
        firmBankingStatus.value(),
        message.value()
    );
  }

  public void success() {
    this.firmBankingStatus = FirmBankingRequestStatus.SUCCESS;
  }

  public record FirmBankingRequestId(String value) {

  }

  public record FromBankName(String value) {

  }

  public record FromBankAccountNumber(String value) {

  }

  public record ToBankName(String value) {

  }

  public record ToBankAccountNumber(String value) {

  }

  public record MoneyAmount(int value) {

  }

  public record FirmBankingStatus(FirmBankingRequestStatus value) {

  }

  public record Message(String value) {

  }
}
