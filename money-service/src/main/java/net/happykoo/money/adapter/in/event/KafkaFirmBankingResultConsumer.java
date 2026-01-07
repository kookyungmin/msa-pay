package net.happykoo.money.adapter.in.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.happykoo.common.annotation.EventAdapter;
import net.happykoo.money.adapter.in.event.payload.FirmBankingResultPayload;
import net.happykoo.money.domain.axon.event.AxonFirmBankingResultEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.kafka.annotation.KafkaListener;

@EventAdapter
@Slf4j
@RequiredArgsConstructor
public class KafkaFirmBankingResultConsumer {

  private final ObjectMapper objectMapper;
  private final EventGateway eventGateway;


  @KafkaListener(
      topics = "${task.firm-banking-result-topic}",
      groupId = "${spring.kafka.group-id}")
  public void receive(ConsumerRecord<String, String> record) {
    try {
      var payload = objectMapper.readValue(record.value(),
          FirmBankingResultPayload.class);
      log.info("firmbanking result received payload = {}", payload);

      //TODO: 원래는 aggregate 생성 후 aggregate 만이 event 발행해야 함
      eventGateway.publish(new AxonFirmBankingResultEvent(
          payload.isSuccess(),
          payload.errorMessage(),
          payload.externalRequestId()
      ));

    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
