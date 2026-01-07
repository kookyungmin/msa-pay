package net.happykoo.money.application.axon.command;


import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record AxonIncreaseMemberMoneyCommand(
    @TargetAggregateIdentifier
    String aggregateId,
    Long membershipId,
    int moneyAmount,
    String rechargingRequestId
) {

}
