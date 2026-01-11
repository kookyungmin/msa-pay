package net.happykoo.money.application.port.in;

import java.util.List;
import net.happykoo.money.application.port.in.command.GetMemberMoneyByMembershipIdsCommand;
import net.happykoo.money.domain.MemberMoney;

public interface GetMemberMoneyUseCase {

  List<MemberMoney> getMemberMoneyList(GetMemberMoneyByMembershipIdsCommand command);

}
