package net.happykoo.banking.application.port.out;

import net.happykoo.banking.application.port.out.data.FirmBankingData;
import net.happykoo.banking.application.port.out.payload.FirmBankingPayload;

public interface RequestFirmBankingPort {

  FirmBankingData requestFirmBanking(FirmBankingPayload payload);

}
