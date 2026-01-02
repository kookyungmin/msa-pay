package net.happykoo.banking.adapter.in.web.request;

public record RequestFirmBankingRequest(
    String fromBankName,
    String fromBankAccountNumber,
    String toBankName,
    String toBankAccountNumber,
    int moneyAmount
) {

}
