package net.happykoo.membership.adapter.in.web;

import lombok.RequiredArgsConstructor;
import net.happykoo.membership.application.port.in.FindMembershipUseCase;
import net.happykoo.membership.application.port.in.command.FindMembershipCommand;
import net.happykoo.membership.common.WebAdapter;
import net.happykoo.membership.domain.Membership;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@WebAdapter
@RequiredArgsConstructor
public class FindMembershipController {

  private final FindMembershipUseCase findMembershipUseCase;

  @GetMapping("/membership/{membershipId}")
  ResponseEntity<Membership> findMembershipByMemberId(@PathVariable String membershipId) {
    FindMembershipCommand command = FindMembershipCommand.builder()
        .membershipId(membershipId)
        .build();

    return ResponseEntity.ok(findMembershipUseCase.findMembership(command));
  }

}
