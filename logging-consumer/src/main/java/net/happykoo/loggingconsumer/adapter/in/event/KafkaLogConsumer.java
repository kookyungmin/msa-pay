package net.happykoo.loggingconsumer.adapter.in.event;

import lombok.extern.slf4j.Slf4j;
import net.happykoo.common.annotation.EventAdapter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

@EventAdapter
@Slf4j
public class KafkaLogConsumer {

  @KafkaListener(
      topics = "${logging.topic}",
      groupId = "${spring.kafka.group-id}")
  public void receive(ConsumerRecord<String, String> record) {
    String payload = record.value();
    log.info("received payload = {}", payload);
  }
}
