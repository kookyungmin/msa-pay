package net.happykoo.banking.application.port.in;

import net.happykoo.banking.application.port.in.command.RegisterBankAccountCommand;
import net.happykoo.banking.domain.RegisteredBankAccount;

public interface RegisterBankAccountUseCase {

  RegisteredBankAccount registerBankAccount(RegisterBankAccountCommand command);

}
