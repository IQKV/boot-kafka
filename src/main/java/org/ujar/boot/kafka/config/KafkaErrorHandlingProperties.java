package org.ujar.boot.kafka.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ujar.boot.kafka.config.errorhandling.Backoff;
import org.ujar.boot.kafka.config.errorhandling.DeadLetter;

@ConfigurationProperties(prefix = "ujar.kafka.error-handling")
public record KafkaErrorHandlingProperties(
    @NotNull @Valid DeadLetter deadLetter,
    @NotNull @Valid Backoff backoff) {


  public DeadLetter getDeadLetter() {
    return deadLetter();
  }

  public Backoff getBackoff() {
    return backoff();
  }
}

