package net.happykoo.membership.adapter.in.web;

import lombok.RequiredArgsConstructor;
import net.happykoo.common.WebAdapter;
import net.happykoo.membership.adapter.in.web.request.ModifyMembershipRequest;
import net.happykoo.membership.application.port.in.ModifyMembershipUseCase;
import net.happykoo.membership.application.port.in.command.ModifyMembershipCommand;
import net.happykoo.membership.domain.Membership;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@WebAdapter
@RequiredArgsConstructor
public class ModifyMembershipController {

  private final ModifyMembershipUseCase modifyMembershipUseCase;

  @PutMapping("/membership/{membershipId}")
  ResponseEntity<Membership> modifyMembership(
      @PathVariable String membershipId,
      @RequestBody ModifyMembershipRequest request) {
    ModifyMembershipCommand command = ModifyMembershipCommand.builder()
        .membershipId(membershipId)
        .name(request.name())
        .address(request.address())
        .email(request.email())
        .isValid(request.isValid())
        .isCorp(request.isCorp())
        .build();

    return ResponseEntity.ok(modifyMembershipUseCase.modifyMembership(command));
  }

}
