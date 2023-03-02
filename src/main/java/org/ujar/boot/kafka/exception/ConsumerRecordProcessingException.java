package org.ujar.boot.kafka.exception;

import java.io.Serial;

public class ConsumerRecordProcessingException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  public ConsumerRecordProcessingException(String message, Throwable cause) {
    super(message, cause);
  }
}
