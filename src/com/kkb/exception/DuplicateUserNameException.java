package com.kkb.exception;

public class DuplicateUserNameException extends Exception{
    public DuplicateUserNameException() {
    }

    public DuplicateUserNameException(String message) {
        super(message);
    }
}
