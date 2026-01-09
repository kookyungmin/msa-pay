package net.happykoo.payment.adapter.out.persistence.jpa;

import net.happykoo.payment.adapter.out.persistence.jpa.entity.JpaPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPaymentRepository extends JpaRepository<JpaPaymentEntity, Long> {

}
