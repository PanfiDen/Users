package com.clearsolutions.users.advice.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String exception) {
        super(exception);
    }
}
