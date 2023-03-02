package org.ujar.boot.kafka.config.topic;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;

public record TopicDefinition(
    @NotNull String name,
    @NotNull Integer partitions,
    @NotNull Duration retention) {

  public String getName() {
    return name();
  }

  public Integer getPartitions() {
    return partitions();
  }

  public Duration getRetention() {
    return retention();
  }
}
