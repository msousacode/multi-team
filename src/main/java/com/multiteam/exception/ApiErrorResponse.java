package com.multiteam.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.UUID;

public class ApiErrorResponse {

    private static final Logger logger = LogManager.getLogger(ApiErrorResponse.class);

    private UUID errorId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
    private LocalDateTime timestamp;
    private Throwable cause;
    private StackTraceElement[] stackTrace;
    private String detailMessage;
    private String action;
    private String classError;
    private int httpCode;

    private ApiErrorResponse(Builder builder) {
        this.errorId = builder.errorId;
        this.timestamp = builder.timestamp;
        this.cause = builder.cause;
        this.stackTrace = builder.stackTrace;
        this.detailMessage = builder.detailMessage;
        this.action = builder.action;
        this.classError = builder.classError;
        this.httpCode = builder.httpCode;
    }

    public static class Builder {

        //mandatory
        private final UUID errorId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
        private final LocalDateTime timestamp;
        private final String detailMessage;
        private final Throwable cause;
        private final StackTraceElement[] stackTrace;
        private final String classError;
        private final int httpCode;

        //optional
        private String action;

        public Builder(String detailMessage, Throwable cause, StackTraceElement[] stackTrace, String classError, int httpCode) {
            this.errorId = UUID.randomUUID();
            this.timestamp = LocalDateTime.now();
            this.detailMessage = detailMessage;
            this.cause = cause;
            this.stackTrace = stackTrace;
            this.classError = classError;
            this.httpCode = httpCode;

            logger.error("error_id {} --- cause: {}", this.errorId, detailMessage);
            logger.error("error_id {} --- trace: {}", this.errorId, stackTrace);
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public ApiErrorResponse build() {
            return new ApiErrorResponse(this);
        }
    }
}


