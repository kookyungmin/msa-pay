package net.happykoo.banking.application.port.out.payload;

public record BankAccountPayload(
    String bankName,
    String bankAccountNumber
) {

}
