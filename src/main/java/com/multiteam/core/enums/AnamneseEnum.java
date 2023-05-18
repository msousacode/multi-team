package com.multiteam.core.enums;

public enum AnamneseEnum {
    OPEN("open"),
    CLOSE("close");

    private final String status;

    AnamneseEnum(String status) {
        this.status = status;
    }
}
