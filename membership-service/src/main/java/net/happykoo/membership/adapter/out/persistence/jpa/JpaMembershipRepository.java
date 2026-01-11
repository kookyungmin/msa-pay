package net.happykoo.membership.adapter.out.persistence.jpa;

import java.util.List;
import net.happykoo.membership.adapter.out.persistence.jpa.entity.JpaMembershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMembershipRepository extends JpaRepository<JpaMembershipEntity, Long> {

  List<JpaMembershipEntity> findByAddress(String address);

}
