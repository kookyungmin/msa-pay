package net.happykoo.payment.adapter.in.web.request;

public record PaymentRequest(
    String requestMembershipId,
    int requestPrice,
    String franchiseId,
    String franchiseFeeRate
) {

}
