package net.happykoo.money.application.port.in.command;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.happykoo.common.validation.SelfValidating;

@Builder
@NoArgsConstructor
@Getter
public class GetMemberMoneyByMembershipIdsCommand extends
    SelfValidating<GetMemberMoneyByMembershipIdsCommand> {

  @Size(min = 1, max = 100)
  private List<String> membershipIds;

  public GetMemberMoneyByMembershipIdsCommand(List<String> membershipIds) {
    this.membershipIds = membershipIds;
    validateSelf();
  }

}
