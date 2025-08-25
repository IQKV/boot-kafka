# ✨ Kafka properties handling

Centralized, safe, and CDI-friendly Kafka client configuration for Java 21 (Jakarta EE) with Lombok.

## Features

- Layered property resolution (file → env → system → programmatic)
- Single model for common/producer/consumer/admin configs
- Validation and safe logging (secrets masked)
- Builders to produce java.util.Properties per client type
- CDI producers for easy injection

## Property keys

- Prefix-based, stripped before use:
  - kafka.common.bootstrap.servers=localhost:9092
  - kafka.producer.acks=all
  - kafka.consumer.group.id=my-group
  - kafka.admin.client.id=admin-1

## Precedence

- Lowest to highest: properties file → environment variables → system properties → programmatic overrides
