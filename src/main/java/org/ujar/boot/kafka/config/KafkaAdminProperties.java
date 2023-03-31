package org.ujar.boot.kafka.config;

import jakarta.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ujar.kafka.admin")
public record KafkaAdminProperties(@NotNull Boolean createTopics) {

  public Boolean isTopicsCreatedByKafkaAdmin() {
    return createTopics();
  }
}

