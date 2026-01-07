package net.happykoo.money.domain.axon.event;

public record AxonRechargeMoneyEvent(
    String rechargingRequestId,
    String memberMoneyEventStreamId,
    Long membershipId,
    int moneyAmount
) {

}
