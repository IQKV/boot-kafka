package org.ujar.boot.kafka.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.ujar.boot.kafka.config.errorhandling.Backoff;
import org.ujar.boot.kafka.exception.ConsumerRecordProcessingException;

@RequiredArgsConstructor
public abstract class BaseKafkaConsumingConfig implements KafkaListenerConfigurer {
  private final LocalValidatorFactoryBean validator;

  protected <V> ConsumerFactory<String, V> consumerFactory(Class<V> valueClass, KafkaProperties kafkaProperties) {
    final var consumerProperties = kafkaProperties.getConsumer().buildProperties();
    try (var serde = new JsonSerde<>(valueClass, new ObjectMapper())) {
      return new DefaultKafkaConsumerFactory<>(consumerProperties,
          new ErrorHandlingDeserializer<>(new StringDeserializer()), new ErrorHandlingDeserializer<>(
          serde.deserializer()));
    }
  }

  protected <V> ConcurrentKafkaListenerContainerFactory<String, V> containerFactory(
      ConsumerFactory<String, V> consumerFactory,
      int threads,
      DefaultErrorHandler errorHandler) {
    ConcurrentKafkaListenerContainerFactory<String, V> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.setConcurrency(threads);

    factory.setCommonErrorHandler(errorHandler);
    return factory;
  }

  @Bean
  protected DefaultErrorHandler kafkaDefaultErrorHandler(
      KafkaOperations<Object, Object> operations,
      KafkaErrorHandlingProperties properties) {
    // Publish to dead letter topic any messages dropped after retries with back off
    final var recoverer = new DeadLetterPublishingRecoverer(operations,
        // Always send to first/only partition of DLT suffixed topic
        (cr, e) -> new TopicPartition(cr.topic() + properties.getDeadLetter().getSuffix(), 0));

    // Spread out attempts over time, taking a little longer between each attempt
    // Set a max for retries below max.poll.interval.ms; default: 5m, as otherwise we trigger a consumer rebalance
    Backoff backoff = properties.getBackoff();
    final var exponentialBackOff = new ExponentialBackOffWithMaxRetries(backoff.getMaxRetries());
    exponentialBackOff.setInitialInterval(backoff.getInitialInterval().toMillis());
    exponentialBackOff.setMultiplier(backoff.getMultiplier());
    exponentialBackOff.setMaxInterval(backoff.getMaxInterval().toMillis());

    // Do not try to recover from validation exceptions when validation of orders failed
    final var errorHandler = new DefaultErrorHandler(recoverer, exponentialBackOff);
    errorHandler.addNotRetryableExceptions(jakarta.validation.ValidationException.class);
    errorHandler.addNotRetryableExceptions(ConsumerRecordProcessingException.class);
    return errorHandler;
  }

  @Override
  public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
    registrar.setValidator(this.validator);
  }
}
