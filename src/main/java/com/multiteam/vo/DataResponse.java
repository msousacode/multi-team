package com.multiteam.vo;

import com.multiteam.persistence.entity.Patient;

public record DataResponse(
        Object content,
        String message,
        Boolean success
) {}
