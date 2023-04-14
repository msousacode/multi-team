package com.multiteam.constants;

public enum ApplicationErrorsEnum {

    TREATMENT_DOES_NOT_EXIST("treatment does not exist"),
    VALUE_DOES_NOT_EMPTY("value can`t be empty or null");

    private String message;

    ApplicationErrorsEnum(String message) {
        this.message = message;
    }
}
