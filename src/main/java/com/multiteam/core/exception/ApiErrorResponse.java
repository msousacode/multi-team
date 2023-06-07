package com.multiteam.core.exception;

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
    private String detailMessage;
    private String action;
    private int httpCode;

    public UUID getErrorId() {
        return errorId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public String getAction() {
        return action;
    }

    public int getHttpCode() {
        return httpCode;
    }

    private ApiErrorResponse(Builder builder) {
        this.errorId = builder.errorId;
        this.timestamp = builder.timestamp;
        this.detailMessage = builder.detailMessage;
        this.action = builder.action;
        this.httpCode = builder.httpCode;
    }

    public static class Builder {

        //mandatory
        private final UUID errorId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
        private final LocalDateTime timestamp;
        private final String detailMessage;
        private final StackTraceElement[] stackTrace;
        private final int httpCode;

        //optional
        private String action;

        public Builder(String detailMessage, StackTraceElement[] stackTrace, int httpCode) {
            this.errorId = UUID.randomUUID();
            this.timestamp = LocalDateTime.now();
            this.detailMessage = detailMessage;
            this.stackTrace = stackTrace;
            this.httpCode = httpCode;

            logger.error("ApiError ERROR_ID: {} DETAIL MESSAGE: {}", this.errorId, detailMessage);
            logger.error("ApiError STACK_TRACE: {} CAUSE: {}", this.errorId, stackTrace);
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


