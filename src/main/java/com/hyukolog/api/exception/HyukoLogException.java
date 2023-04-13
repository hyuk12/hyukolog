package com.hyukolog.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class HyukoLogException extends RuntimeException {

    private final Map<String, String> validation = new HashMap<>();

    public HyukoLogException(String message) {
        super(message);
    }

    public HyukoLogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
