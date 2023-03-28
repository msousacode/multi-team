package com.multiteam.model;

public class DataResponse<T> {
        T content;
        String message;
        Boolean success;

    public DataResponse(T content, String message, Boolean success) {
        this.content = content;
        this.message = message;
        this.success = success;
    }

    public T getContent() {
        return content;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getSuccess() {
        return success;
    }
}
