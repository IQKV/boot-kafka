package org.ujar.boot.kafka.config.errorhandling;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;

public record DeadLetter(
    @NotNull Duration retention,
    @Nullable String suffix) {

  public Duration getRetention() {
    return retention();
  }

  public String getSuffix() {
    return suffix();
  }
}
