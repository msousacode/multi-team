package com.multiteam.core.exception.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ApiExceptionDetails implements Serializable {

  private final int status;
  private final String errorDetail;

  private final Instant timestamp = Instant.now();

  public ApiExceptionDetails(HttpStatus status, String errorDetail) {
    this.status = status.value();
    this.errorDetail = errorDetail;
  }

  public int getStatus() {
    return status;
  }

  public Instant getTimestamp() {
    return timestamp;
  }
}
