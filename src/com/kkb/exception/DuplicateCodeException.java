package com.kkb.exception;

public class DuplicateCodeException extends Exception{
    public DuplicateCodeException() {
    }

    public DuplicateCodeException(String message) {
        super(message);
    }
}
