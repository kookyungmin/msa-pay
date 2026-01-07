package net.happykoo.money.domain.axon.event;


public record AxonIncreaseMemberMoneyEvent(
    String aggregateId,
    Long membershipId,
    int moneyAmount,
    String rechargingRequestId
) {

}
