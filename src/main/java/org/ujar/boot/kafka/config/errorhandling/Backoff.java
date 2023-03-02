package org.ujar.boot.kafka.config.errorhandling;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Duration;

public record Backoff(
    @NotNull Duration initialInterval,
    @NotNull Duration maxInterval,
    @Positive int maxRetries,
    @Positive double multiplier) {

  public Duration getInitialInterval() {
    return initialInterval();
  }

  public Duration getMaxInterval() {
    return maxInterval();
  }

  public int getMaxRetries() {
    return maxRetries();
  }

  public double getMultiplier() {
    return multiplier();
  }
}
