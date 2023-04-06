package com.multiteam.constants;

public enum ApplicationErrorsEnum {

    TREATMENT_DOES_NOT_EXIST("treatment does not exist");

    private String message;

    ApplicationErrorsEnum(String message) {
        this.message = message;
    }
}
