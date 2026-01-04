package net.happykoo.banking.application.port.out;

import net.happykoo.banking.domain.RegisteredBankAccount;

public interface RequestMembershipPort {

  boolean existsMembership(RegisteredBankAccount.MembershipId membershipId);
}
