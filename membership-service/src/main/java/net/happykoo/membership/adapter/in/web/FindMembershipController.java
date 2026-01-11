package net.happykoo.membership.adapter.in.web;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.happykoo.common.annotation.WebAdapter;
import net.happykoo.membership.application.port.in.FindMembershipUseCase;
import net.happykoo.membership.application.port.in.command.FindMembershipByAddressCommand;
import net.happykoo.membership.application.port.in.command.FindMembershipCommand;
import net.happykoo.membership.domain.Membership;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//TODO: 원래는 useCase 결과값은 result dto, controller의 결과 값은 response로 변환 과정을 거쳐야 함

@WebAdapter
@RequiredArgsConstructor
public class FindMembershipController {

  private final FindMembershipUseCase findMembershipUseCase;

  @GetMapping("/membership/{membershipId}")
  ResponseEntity<Membership> findMembershipByMemberId(@PathVariable String membershipId) {
    var command = FindMembershipCommand.builder()
        .membershipId(membershipId)
        .build();

    return ResponseEntity.ok(findMembershipUseCase.findMembership(command));
  }

  @GetMapping("/membership/by-address/{address}")
  ResponseEntity<List<Membership>> findMembershipsByAddress(@PathVariable String address) {
    var command = FindMembershipByAddressCommand.builder()
        .address(address)
        .build();

    return ResponseEntity.ok(findMembershipUseCase.findMembershipsByAddress(command));
  }
}
