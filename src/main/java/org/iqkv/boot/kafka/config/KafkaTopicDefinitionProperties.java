package org.iqkv.boot.kafka.config;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

import org.iqkv.boot.kafka.config.topic.TopicDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "iqkv.kafka")
public record KafkaTopicDefinitionProperties(@NotNull Map<String, TopicDefinition> topics) {

  public Map<String, TopicDefinition> getTopics() {
    return topics();
  }

  public TopicDefinition get(String key) {
    return topics.get(key);
  }
}

