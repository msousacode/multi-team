package com.multiteam.controller.dto;

public record ResponseDto(
        Object content,
        String message,
        Boolean success
) {}
