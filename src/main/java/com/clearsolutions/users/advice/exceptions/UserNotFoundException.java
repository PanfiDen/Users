package com.clearsolutions.users.advice.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("User with such credentials is not found");
    }
}
