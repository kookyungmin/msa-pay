package net.happykoo.banking.adapter.out.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.happykoo.banking.application.port.out.SendFirmBankingResultPort;
import net.happykoo.banking.application.port.out.payload.SendFirmBankingResultPayload;
import net.happykoo.common.annotation.EventAdapter;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.core.KafkaTemplate;

@EventAdapter
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaTopicProps.class)
@Slf4j
public class KafkaFirmBankingProducer implements SendFirmBankingResultPort {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final KafkaTopicProps kafkaTopicProps;
  private final ObjectMapper objectMapper;

  @Override
  public void sendFirmBankingResult(SendFirmBankingResultPayload payload) {
    try {
      String key = payload.externalRequestId();
      String value = objectMapper.writeValueAsString(payload);
      kafkaTemplate.send(kafkaTopicProps.firmBankingResultTopic(), key, value)
          .whenComplete((result, ex) -> {
            if (ex != null) {
              //전송 실패
              log.error(
                  "Kafka send failed. topic={}, key={}",
                  kafkaTopicProps.firmBankingResultTopic(),
                  key,
                  ex
              );
            } else {
              //전송 성공
              RecordMetadata meta = result.getRecordMetadata();
              log.info(
                  "Kafka send success. topic={}, key={}, partition={}, offset={}",
                  meta.topic(),
                  key,
                  meta.partition(),
                  meta.offset()
              );
            }
          });
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
