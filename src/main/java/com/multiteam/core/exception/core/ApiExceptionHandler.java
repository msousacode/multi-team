package com.multiteam.core.exception.core;

import com.multiteam.core.exception.ApiException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  private static final String UNEXPECTED_ERROR_MESSAGE = "Unexpected Error";

  @ExceptionHandler
  public ResponseEntity<Object> handleUnexpectedError(Exception ex) {
    return unwrapApiException(ex)
        .map(this::handleApiException)
        .orElseGet(
            () -> {
              log.error(UNEXPECTED_ERROR_MESSAGE, ex);
              return toReponseEntity(
                  new ApiExceptionDetails(HttpStatus.INTERNAL_SERVER_ERROR,
                      ApiExceptionCodes.UNEXPECTED_ERROR.getErrorDescription()));
            });
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return unwrapApiException(ex)
        .map(this::handleApiException)
        .orElseGet(
            () -> {
              log.error(UNEXPECTED_ERROR_MESSAGE, ex);
              return toReponseEntity(
                  new ApiExceptionDetails(HttpStatus.INTERNAL_SERVER_ERROR,
                      ApiExceptionCodes.UNEXPECTED_ERROR.getErrorDescription()));
            });
  }

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<Object> handleApiException(ApiException apiException) {
    ApiExceptionDetails detail = apiException.getDetail();
    log.error(
        "API exception. httpStatus='{}'; errorDetail='{}'",
        detail.getStatus(),
        detail.getErrorDetail(),
        apiException);
    return toReponseEntity(apiException.getDetail());
  }

  private Optional<ApiException> unwrapApiException(Throwable ex) {
    if (ex instanceof ApiException) {
      return Optional.of((ApiException) ex);
    }
    return Optional.empty();
  }

  private ResponseEntity<Object> toReponseEntity(ApiExceptionDetails detail) {
    return ResponseEntity.status(detail.getStatus()).body(detail);
  }
}
