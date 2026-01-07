package net.happykoo.money.adapter.in.event.payload;

public record FirmBankingResultPayload(
    boolean isSuccess,
    String errorMessage,
    String externalRequestId
) {

}
