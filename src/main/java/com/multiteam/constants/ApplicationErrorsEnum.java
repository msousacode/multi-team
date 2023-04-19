package com.multiteam.constants;

public enum ApplicationErrorsEnum {

    TREATMENT_DOES_NOT_EXIST("treatment does not exist"),
    VALUE_DOES_NOT_EMPTY("value can`t be empty or null"),
    CLINIC_IS_MANDATORY("patient needs to be associated with the clinic");

    private String message;

    ApplicationErrorsEnum(String message) {
        this.message = message;
    }
}
