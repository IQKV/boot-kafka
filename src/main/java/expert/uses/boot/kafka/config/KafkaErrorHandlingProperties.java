/*
 * Copyright 2025 Expertness Team, and the original author or authors from the JHipster project.
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

package expert.uses.boot.kafka.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import expert.uses.boot.kafka.config.errorhandling.Backoff;
import expert.uses.boot.kafka.config.errorhandling.DeadLetter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "expertness.kafka.error-handling")
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

