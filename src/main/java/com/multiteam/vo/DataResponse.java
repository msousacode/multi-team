package com.multiteam.vo;

public record DataResponse(
        Object content,
        String message,
        Boolean success
){}
