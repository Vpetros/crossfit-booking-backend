package com.crossfit.booking.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;


@Getter
public class ValidationErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final Map<String, String> errors;

    public ValidationErrorResponse(int status, String error, Map<String, String> errors) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.errors = errors;
    }


}
