package net.happykoo.money.adapter.in.web;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.happykoo.common.annotation.WebAdapter;
import net.happykoo.money.adapter.in.web.request.GetMemberMoneyByMembershipIdsRequest;
import net.happykoo.money.application.port.in.GetMemberMoneyUseCase;
import net.happykoo.money.application.port.in.command.GetMemberMoneyByMembershipIdsCommand;
import net.happykoo.money.domain.MemberMoney;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@WebAdapter
@RequiredArgsConstructor
public class FindMemberMoneyController {

  private final GetMemberMoneyUseCase getMemberMoneyUseCase;

  @PostMapping("/money/member-money/by-membership-id")
  ResponseEntity<List<MemberMoney>> findMemberMoneyByMembershipId(@RequestBody
  GetMemberMoneyByMembershipIdsRequest request) {
    return ResponseEntity.ok(getMemberMoneyUseCase.getMemberMoneyList(
        new GetMemberMoneyByMembershipIdsCommand(request.membershipIds())
    ));
  }

}
