package net.happykoo.membership.dummy;

import java.util.List;
import java.util.stream.IntStream;
import net.happykoo.membership.adapter.out.persistence.jpa.JpaMembershipRepository;
import net.happykoo.membership.adapter.out.persistence.jpa.entity.JpaMembershipEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Rollback(false)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenerateMembershipDataTest {

  private static final List<String> ADDRESS_GU = List.of(
      "강남구",
      "서초구",
      "관악구"
  );

  @Autowired
  private JpaMembershipRepository membershipRepository;

  @Test
  void generate() {
    List<JpaMembershipEntity> entities = IntStream.range(1, 1000)
        .mapToObj(index ->
            new JpaMembershipEntity(
                "테스터" + index,
                "test" + index + "@test.com",
                ADDRESS_GU.get(index % 3),
                true,
                index % 2 == 0
            )
        )
        .toList();

    membershipRepository.saveAll(entities);
  }
}