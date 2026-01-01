package net.happykoo.membership.adapter.in.web.request;

public record ModifyMembershipRequest(
    String name,
    String address,
    String email,
    boolean isValid,
    boolean isCorp
) {

}
