package net.happykoo.banking.adapter.out.persistence;

import net.happykoo.banking.adapter.out.persistence.jpa.entity.JpaFirmBankingRequestEntity;
import net.happykoo.banking.domain.FirmBankingRequest;
import net.happykoo.banking.domain.FirmBankingRequest.FirmBankingRequestId;
import net.happykoo.banking.domain.FirmBankingRequest.FirmBankingStatus;
import net.happykoo.banking.domain.FirmBankingRequest.FromBankAccountNumber;
import net.happykoo.banking.domain.FirmBankingRequest.FromBankName;
import net.happykoo.banking.domain.FirmBankingRequest.Message;
import net.happykoo.banking.domain.FirmBankingRequest.MoneyAmount;
import net.happykoo.banking.domain.FirmBankingRequest.ToBankAccountNumber;
import net.happykoo.banking.domain.FirmBankingRequest.ToBankName;
import org.springframework.stereotype.Component;

@Component
public class FirmBankingRequestMapper {

  FirmBankingRequest mapToDomainEntity(JpaFirmBankingRequestEntity entity) {
    return FirmBankingRequest.generateFirmBankingRequest(
        new FirmBankingRequestId(entity.getId().toString()),
        new FromBankName(entity.getFromBankName()),
        new FromBankAccountNumber(entity.getFromBankAccountNumber()),
        new ToBankName(entity.getToBankName()),
        new ToBankAccountNumber(entity.getToBankAccountNumber()),
        new MoneyAmount(entity.getMoneyAmount()),
        new FirmBankingStatus(entity.getStatus()),
        new Message(entity.getErrorMsg())
    );
  }
}
