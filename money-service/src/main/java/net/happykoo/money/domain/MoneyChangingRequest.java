package net.happykoo.money.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MoneyChangingRequest {

  private final String moneyChangingRequestId;
  private final String targetMembershipId;
  private final MoneyChangingRequestType requestType;
  private final int moneyAmount;
  private MoneyChangingRequestStatus requestStatus;
  private String message;

  public static MoneyChangingRequest generateMoneyChangingRequest(
      MoneyChangingRequestId moneyChangingRequestId,
      TargetMembershipId targetMembershipId,
      RequestType requestType,
      MoneyAmount moneyAmount,
      RequestStatus status,
      Message message
  ) {
    return new MoneyChangingRequest(
        moneyChangingRequestId.value(),
        targetMembershipId.value(),
        requestType.value(),
        moneyAmount.value(),
        status.value(),
        message.value()
    );
  }

  public void success() {
    this.requestStatus = MoneyChangingRequestStatus.SUCCESS;
  }

  public record MoneyChangingRequestId(String value) {

  }

  public record TargetMembershipId(String value) {

  }

  public record RequestType(MoneyChangingRequestType value) {

  }

  public record MoneyAmount(int value) {

  }

  public record RequestStatus(MoneyChangingRequestStatus value) {

  }

  public record Message(String value) {

  }

}
