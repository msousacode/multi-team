package com.multiteam.core.exception;

import com.multiteam.core.exception.core.ApiExceptionDetails;
import lombok.Getter;

public abstract class ApiException extends RuntimeException {

  @Getter
  private final ApiExceptionDetails detail;

  protected ApiException(ApiExceptionDetails detail) {
    this.detail = detail;
  }

  protected ApiException(ApiExceptionDetails detail, Throwable cause) {
    super(cause);
    this.detail = detail;
  }

  protected ApiException(String message, ApiExceptionDetails detail, Throwable cause) {
    super(message, cause);
    this.detail = detail;
  }
}
