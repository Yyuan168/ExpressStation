package com.kkb.exception;

public class DuplicateUserPhoneException extends Exception{
    public DuplicateUserPhoneException() {
    }

    public DuplicateUserPhoneException(String message) {
        super(message);
    }
}
