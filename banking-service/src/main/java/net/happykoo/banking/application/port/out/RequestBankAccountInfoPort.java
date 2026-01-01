package net.happykoo.banking.application.port.out;

import net.happykoo.banking.application.port.out.payload.BankAccountPayload;
import net.happykoo.banking.application.port.out.data.BankAccountData;

public interface RequestBankAccountInfoPort {

  BankAccountData requestBankAccountInfo(BankAccountPayload query);
}
