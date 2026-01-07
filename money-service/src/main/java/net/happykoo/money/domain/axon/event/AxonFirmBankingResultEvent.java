package net.happykoo.money.domain.axon.event;

public record AxonFirmBankingResultEvent(
    boolean isSuccess,
    String errorMessage,
    String rechargingRequestId
) {

}
