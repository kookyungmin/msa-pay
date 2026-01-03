package net.happykoo.common.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(LoggingProps.class)
@RequiredArgsConstructor
@Slf4j
public class LoggingProducer {

  private final LoggingProps loggingProps;
  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendMessage(String key, String message) {
    kafkaTemplate
        .send(loggingProps.topic(), key, message)
        .whenComplete((result, ex) -> {
          if (ex != null) {
            //전송 실패
            log.error(
                "Kafka send failed. topic={}, key={}",
                loggingProps.topic(),
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
  }

}
