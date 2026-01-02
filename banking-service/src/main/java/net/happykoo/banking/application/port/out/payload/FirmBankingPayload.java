package net.happykoo.banking.application.port.out.payload;

public record FirmBankingPayload(
    String fromBankName,
    String fromBankAccountNumber,
    String toBankName,
    String toBankAccountNumber,
    int moneyAmount
) {

}
