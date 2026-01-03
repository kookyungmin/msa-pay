package net.happykoo.membership.adapter.in.web;

import lombok.RequiredArgsConstructor;
import net.happykoo.common.annotation.WebAdapter;
import net.happykoo.membership.adapter.in.web.request.RegisterMembershipRequest;
import net.happykoo.membership.application.port.in.RegisterMembershipUseCase;
import net.happykoo.membership.application.port.in.command.RegisterMembershipCommand;
import net.happykoo.membership.domain.Membership;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//TODO: 원래는 useCase 결과값은 result dto, controller의 결과 값은 response로 변환 과정을 거쳐야 함

@WebAdapter
@RequiredArgsConstructor
public class RegisterMembershipController {

  private final RegisterMembershipUseCase registerMembershipUseCase;

  @PostMapping("/membership")
  ResponseEntity<Membership> registerMembership(@RequestBody RegisterMembershipRequest request) {
    var command = RegisterMembershipCommand.builder()
        .name(request.name())
        .address(request.address())
        .email(request.email())
        .isCorp(request.isCorp())
        .build();
    var membership = registerMembershipUseCase.registerMembership(command);

    return ResponseEntity.ok(membership);
  }
}
