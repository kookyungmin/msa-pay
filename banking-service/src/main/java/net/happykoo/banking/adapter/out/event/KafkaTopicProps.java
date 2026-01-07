package net.happykoo.banking.adapter.out.event;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "task")
public record KafkaTopicProps(
    String firmBankingTopic,
    String firmBankingResultTopic
) {

}
