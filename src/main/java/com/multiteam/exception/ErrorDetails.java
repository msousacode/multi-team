package com.multiteam.exception;

import java.time.LocalDateTime;

public record ErrorDetails(
        String message,
        LocalDateTime timestamp
) {}
