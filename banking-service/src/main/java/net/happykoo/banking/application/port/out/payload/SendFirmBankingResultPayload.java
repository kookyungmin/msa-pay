package net.happykoo.banking.application.port.out.payload;

public record SendFirmBankingResultPayload(
    boolean isSuccess,
    String errorMessage,
    String externalRequestId
) {

}
