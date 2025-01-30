/*
 * Copyright 2025 IQKV Team, and the original author or authors from the JHipster project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iqkv.boot.kafka.config.errorhandling;

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
