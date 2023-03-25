package com.multiteam.persistence.enuns;

public enum MessageSystemEnuns {

    VALUE_NOT_BE_EMPTY("value not`be empty");

    private final String message;

    MessageSystemEnuns(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
