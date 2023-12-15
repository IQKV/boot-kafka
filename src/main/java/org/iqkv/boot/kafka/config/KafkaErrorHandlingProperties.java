package org.iqkv.boot.kafka.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.iqkv.boot.kafka.config.errorhandling.Backoff;
import org.iqkv.boot.kafka.config.errorhandling.DeadLetter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "iqkv.kafka.error-handling")
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

