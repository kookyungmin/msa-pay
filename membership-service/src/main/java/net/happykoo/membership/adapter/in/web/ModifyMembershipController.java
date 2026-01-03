package net.happykoo.membership.adapter.in.web;

import lombok.RequiredArgsConstructor;
import net.happykoo.common.annotation.WebAdapter;
import net.happykoo.membership.adapter.in.web.request.ModifyMembershipRequest;
import net.happykoo.membership.application.port.in.ModifyMembershipUseCase;
import net.happykoo.membership.application.port.in.command.ModifyMembershipCommand;
import net.happykoo.membership.domain.Membership;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

//TODO: 원래는 useCase 결과값은 result dto, controller의 결과 값은 response로 변환 과정을 거쳐야 함

@WebAdapter
@RequiredArgsConstructor
public class ModifyMembershipController {

  private final ModifyMembershipUseCase modifyMembershipUseCase;

  @PutMapping("/membership/{membershipId}")
  ResponseEntity<Membership> modifyMembership(
      @PathVariable String membershipId,
      @RequestBody ModifyMembershipRequest request) {
    var command = ModifyMembershipCommand.builder()
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
