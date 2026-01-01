package net.happykoo.membership.adapter.in.web;

import lombok.RequiredArgsConstructor;
import net.happykoo.common.WebAdapter;
import net.happykoo.membership.adapter.in.web.request.RegisterMembershipRequest;
import net.happykoo.membership.application.port.in.RegisterMembershipUseCase;
import net.happykoo.membership.application.port.in.command.RegisterMembershipCommand;
import net.happykoo.membership.domain.Membership;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@WebAdapter
@RequiredArgsConstructor
public class RegisterMembershipController {

  private final RegisterMembershipUseCase registerMembershipUseCase;

  @PostMapping("/membership")
  ResponseEntity<Membership> registerMembership(@RequestBody RegisterMembershipRequest request) {
    RegisterMembershipCommand command = RegisterMembershipCommand.builder()
        .name(request.name())
        .address(request.address())
        .email(request.email())
        .isCorp(request.isCorp())
        .build();
    Membership membership = registerMembershipUseCase.registerMembership(command);

    return ResponseEntity.ok(membership);
  }
}
