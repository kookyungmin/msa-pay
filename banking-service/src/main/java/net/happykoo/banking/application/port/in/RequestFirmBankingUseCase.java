package net.happykoo.banking.application.port.in;

import net.happykoo.banking.application.port.in.command.RequestFirmBankingCommand;
import net.happykoo.banking.domain.FirmBankingRequest;

public interface RequestFirmBankingUseCase {

  FirmBankingRequest requestFirmBanking(RequestFirmBankingCommand command);
}
