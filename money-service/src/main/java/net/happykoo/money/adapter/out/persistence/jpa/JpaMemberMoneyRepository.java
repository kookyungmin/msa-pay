package net.happykoo.money.adapter.out.persistence.jpa;

import java.util.List;
import java.util.Optional;
import net.happykoo.money.adapter.out.persistence.jpa.entity.JpaMemberMoneyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMemberMoneyRepository extends JpaRepository<JpaMemberMoneyEntity, Long> {

  Optional<JpaMemberMoneyEntity> findByMembershipId(String membershipId);

  boolean existsByMembershipId(String membershipId);

  List<JpaMemberMoneyEntity> findAllByMembershipIdIn(List<String> membershipIds);
}
