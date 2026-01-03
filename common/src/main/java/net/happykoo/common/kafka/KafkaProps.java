package net.happykoo.common.kafka;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProps(
    String[] bootstrapServers,
    String groupId
) {

}
