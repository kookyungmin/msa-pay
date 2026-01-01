package net.happykoo.banking.adapter.in.web.request;

public record RegisterBankAccountRequest(
    String membershipId,
    String bankName,
    String bankAccountNumber
) {

}
