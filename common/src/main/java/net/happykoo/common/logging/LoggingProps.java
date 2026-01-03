package net.happykoo.common.logging;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "logging")
public record LoggingProps(
    String topic
) {

}
