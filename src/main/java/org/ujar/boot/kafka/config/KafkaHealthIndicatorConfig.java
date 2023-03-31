package org.ujar.boot.kafka.config;

import java.util.concurrent.ExecutionException;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@ConditionalOnEnabledHealthIndicator("kafka")
@ConditionalOnBean(KafkaAdmin.class)
@AutoConfigureAfter(KafkaAutoConfiguration.class)
@EnableConfigurationProperties(KafkaHealthIndicatorProperties.class)
@RequiredArgsConstructor
public class KafkaHealthIndicatorConfig {

  private final KafkaAdmin admin;

  @Bean
  public HealthIndicator kafkaHealthIndicator(KafkaHealthIndicatorProperties properties) {
    final var describeClusterOptions = new DescribeClusterOptions().timeoutMs(properties.getResponseTimeout());
    return new HealthIndicator() {

      private final AdminClient adminClient = AdminClient.create(admin.getConfigurationProperties());

      @Override
      public Health health() {
        final DescribeClusterResult describeCluster = adminClient.describeCluster(describeClusterOptions);
        try {
          final String clusterId = describeCluster.clusterId().get();
          final int nodeCount = describeCluster.nodes().get().size();
          return Health.up()
              .withDetail("clusterId", clusterId)
              .withDetail("nodeCount", nodeCount)
              .build();
        } catch (ExecutionException e) {
          return Health.down()
              .withException(e)
              .build();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          return Health.down()
              .withException(e)
              .build();
        }
      }
    };
  }
}
