package com.multiteam.core.exception.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApiExceptionCodes {
  UNEXPECTED_ERROR(500, "unexpected error"),
  RESOURCE_NOT_FOUND(404, "Resource not found");

  private final int statusCode;
  private final String errorDescription;

  public String getErrorDescription() {
    return this.name();
  }
}
