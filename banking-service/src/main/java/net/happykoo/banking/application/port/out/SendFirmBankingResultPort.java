package net.happykoo.banking.application.port.out;

import net.happykoo.banking.application.port.out.payload.SendFirmBankingResultPayload;

public interface SendFirmBankingResultPort {

  void sendFirmBankingResult(SendFirmBankingResultPayload payload);
}
