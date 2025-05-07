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

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerde;


public abstract class BaseKafkaProducingConfig {

  protected <V> ProducerFactory<String, V> producerFactory(Class<V> valueClass,
                                                           KafkaProperties kafkaProperties,
                                                           SslBundles sslBundles) {
    try (var serde = new JsonSerde<>(valueClass, new ObjectMapper())) {
      final var producerProperties = kafkaProperties.getProducer().buildProperties(sslBundles);
      final var producerFactory = new DefaultKafkaProducerFactory<>(producerProperties,
          new StringSerializer(),
          serde.serializer());
      producerFactory.setTransactionIdPrefix(getTransactionPrefix());
      return producerFactory;
    }
  }

  protected <V> KafkaTemplate<String, V> kafkaTemplate(
      ProducerFactory<String, V> producerFactory) {
    return new KafkaTemplate<>(producerFactory);
  }

  protected String getTransactionPrefix() {
    return "tx-" + UUID.randomUUID() + "-";
  }
}
