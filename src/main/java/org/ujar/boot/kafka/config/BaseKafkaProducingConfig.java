package org.ujar.boot.kafka.config;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerde;


public abstract class BaseKafkaProducingConfig {

  protected <V> ProducerFactory<String, V> producerFactory(Class<V> valueClass, KafkaProperties kafkaProperties) {
    try (var serde = new JsonSerde<>(valueClass, new ObjectMapper())) {
      final var producerProperties = kafkaProperties.getProducer().buildProperties();
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
