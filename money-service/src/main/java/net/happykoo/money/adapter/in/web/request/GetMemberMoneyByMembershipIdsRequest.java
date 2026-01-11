package net.happykoo.money.adapter.in.web.request;

import java.util.List;

public record GetMemberMoneyByMembershipIdsRequest(
    List<String> membershipIds
) {

}
