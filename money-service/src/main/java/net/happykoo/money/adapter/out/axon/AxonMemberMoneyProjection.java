package net.happykoo.money.adapter.out.axon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.happykoo.common.annotation.EventAdapter;
import net.happykoo.money.adapter.out.persistence.jpa.JpaMemberMoneyRepository;
import net.happykoo.money.adapter.out.persistence.jpa.entity.JpaMemberMoneyEntity;
import net.happykoo.money.domain.axon.event.AxonCreateMemberMoneyEvent;
import net.happykoo.money.domain.axon.event.AxonIncreaseMemberMoneyEvent;
import org.axonframework.eventhandling.EventHandler;

@EventAdapter
@RequiredArgsConstructor
@Slf4j
public class AxonMemberMoneyProjection {

  private final JpaMemberMoneyRepository jpaMemberMoneyRepository;

  @EventHandler
  public void on(AxonCreateMemberMoneyEvent event) {
    log.info("AxonCreateMemberMoneyEvent Handler >>> {}", event);
    var entity = new JpaMemberMoneyEntity(
        String.valueOf(event.membershipId()),
        0,
        event.aggregateId()
    );
    jpaMemberMoneyRepository.save(entity);
  }

  @EventHandler
  public void on(AxonIncreaseMemberMoneyEvent event) {
    //TODO: 재시도를 고려하여 멱등성 있게 변경해야함
    log.info("AxonIncreaseMemberMoneyEvent Handler >>> {}", event);
    jpaMemberMoneyRepository.findByMembershipId(String.valueOf(event.membershipId()))
        .ifPresentOrElse(
            entity -> {
              entity.setBalance(entity.getBalance() + event.moneyAmount());
              jpaMemberMoneyRepository.save(entity);
            }, () -> {
              log.error("entity does not exists : {}", event.membershipId());
            }
        );
  }
}
