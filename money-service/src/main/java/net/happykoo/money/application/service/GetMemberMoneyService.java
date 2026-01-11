package net.happykoo.money.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.happykoo.common.annotation.UseCase;
import net.happykoo.money.application.port.in.GetMemberMoneyUseCase;
import net.happykoo.money.application.port.in.command.GetMemberMoneyByMembershipIdsCommand;
import net.happykoo.money.application.port.out.FindMemberMoneyPort;
import net.happykoo.money.domain.MemberMoney;

@UseCase
@RequiredArgsConstructor
public class GetMemberMoneyService implements GetMemberMoneyUseCase {

  private final FindMemberMoneyPort findMemberMoneyPort;

  @Override
  public List<MemberMoney> getMemberMoneyList(GetMemberMoneyByMembershipIdsCommand command) {
    return findMemberMoneyPort.findAllMemberMoneyByMembershipIds(command.getMembershipIds());
  }
}
