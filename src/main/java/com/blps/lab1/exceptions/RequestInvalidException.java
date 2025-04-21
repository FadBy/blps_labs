package com.blps.lab1.exceptions;

public class RequestInvalidException extends RuntimeException {
    public RequestInvalidException(String message) {
        super(message);
    }
}
