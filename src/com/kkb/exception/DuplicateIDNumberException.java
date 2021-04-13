package com.kkb.exception;

public class DuplicateIDNumberException extends Exception{
    public DuplicateIDNumberException() {
    }

    public DuplicateIDNumberException(String message) {
        super(message);
    }
}
