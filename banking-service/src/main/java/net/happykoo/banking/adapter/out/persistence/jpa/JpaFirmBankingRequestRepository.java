package net.happykoo.banking.adapter.out.persistence.jpa;

import net.happykoo.banking.adapter.out.persistence.jpa.entity.JpaFirmBankingRequestEntity;
import net.happykoo.banking.domain.FirmBankingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFirmBankingRequestRepository extends
    JpaRepository<JpaFirmBankingRequestEntity, Long> {

}
