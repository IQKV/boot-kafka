package org.ujar.boot.kafka.config;

import jakarta.validation.constraints.NotNull;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@ConfigurationProperties(prefix = "management.health.kafka")
public class KafkaHealthIndicatorProperties {
  @NotNull Boolean enabled;
  @NotNull Integer responseTimeout;
}
