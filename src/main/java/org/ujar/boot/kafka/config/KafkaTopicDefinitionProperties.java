package org.ujar.boot.kafka.config;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ujar.boot.kafka.config.topic.TopicDefinition;

@ConfigurationProperties(prefix = "ujar.kafka")
public record KafkaTopicDefinitionProperties(@NotNull Map<String, TopicDefinition> topics) {

  public Map<String, TopicDefinition> getTopics() {
    return topics();
  }

  public TopicDefinition get(String key) {
    return topics.get(key);
  }
}

