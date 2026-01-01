package net.happykoo.membership.application.port.in;

import net.happykoo.membership.application.port.in.command.ModifyMembershipCommand;
import net.happykoo.membership.domain.Membership;

public interface ModifyMembershipUseCase {

  Membership modifyMembership(ModifyMembershipCommand command);
}
