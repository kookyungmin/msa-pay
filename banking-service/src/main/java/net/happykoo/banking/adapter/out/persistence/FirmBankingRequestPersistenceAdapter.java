package net.happykoo.banking.adapter.out.persistence;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.happykoo.banking.adapter.out.persistence.jpa.JpaFirmBankingRequestRepository;
import net.happykoo.banking.adapter.out.persistence.jpa.entity.JpaFirmBankingRequestEntity;
import net.happykoo.banking.application.port.out.SaveFirmBankingRequestPort;
import net.happykoo.banking.domain.FirmBankingRequest;
import net.happykoo.banking.domain.FirmBankingRequest.FirmBankingRequestId;
import net.happykoo.banking.domain.FirmBankingRequest.FirmBankingStatus;
import net.happykoo.banking.domain.FirmBankingRequest.FromBankAccountNumber;
import net.happykoo.banking.domain.FirmBankingRequest.FromBankName;
import net.happykoo.banking.domain.FirmBankingRequest.MoneyAmount;
import net.happykoo.banking.domain.FirmBankingRequest.ToBankAccountNumber;
import net.happykoo.banking.domain.FirmBankingRequest.ToBankName;
import net.happykoo.banking.domain.FirmBankingRequestStatus;
import net.happykoo.common.PersistenceAdapter;

@PersistenceAdapter
@RequiredArgsConstructor
public class FirmBankingRequestPersistenceAdapter implements SaveFirmBankingRequestPort {

  private final JpaFirmBankingRequestRepository jpaFirmBankingRequestRepository;
  private final FirmBankingRequestMapper firmBankingRequestMapper;

  @Override
  public FirmBankingRequest createFirmBankingRequest(
      FromBankName fromBankName,
      FromBankAccountNumber fromBankAccountNumber,
      ToBankName toBankName,
      ToBankAccountNumber toBankAccountNumber,
      MoneyAmount moneyAmount
  ) {
    var entity = new JpaFirmBankingRequestEntity(
        fromBankName.value(),
        fromBankAccountNumber.value(),
        toBankName.value(),
        toBankAccountNumber.value(),
        moneyAmount.value(),
        FirmBankingRequestStatus.REQUESTED,
        null
    );

    return firmBankingRequestMapper.mapToDomainEntity(jpaFirmBankingRequestRepository.save(entity));
  }

  @Override
  public FirmBankingRequest updateFirmBankingStatus(
      FirmBankingRequestId firmBankingRequestId,
      FirmBankingStatus firmBankingStatus) {
    var entity = jpaFirmBankingRequestRepository.findById(
            Long.parseLong(firmBankingRequestId.value())
        )
        .orElseThrow(() -> new EntityNotFoundException(
            "entity does not exists : " + firmBankingRequestId.value()));
    entity.setStatus(firmBankingStatus.value());

    jpaFirmBankingRequestRepository.save(entity);

    return firmBankingRequestMapper.mapToDomainEntity(entity);
  }
}
