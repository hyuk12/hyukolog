package com.hyukolog.api.exception;

import lombok.Getter;

@Getter
public class InvalidRequest extends HyukoLogException{

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(Throwable cause) {
        super(MESSAGE, cause);
    }

    public InvalidRequest(String fieldName, String errorMessage) {
        super(MESSAGE);
        addValidation(fieldName, errorMessage);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
