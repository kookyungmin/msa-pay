package net.happykoo.money.application.port.out;

import java.util.List;
import net.happykoo.money.domain.MemberMoney;
import net.happykoo.money.domain.MemberMoney.MembershipId;

public interface FindMemberMoneyPort {

  MemberMoney findMemberMoneyByMembershipId(
      MemberMoney.MembershipId membershipId
  );

  boolean existsMemberMoneyByMembershipId(MembershipId membershipId);

  List<MemberMoney> findAllMemberMoneyByMembershipIds(List<String> membershipIds);
}
