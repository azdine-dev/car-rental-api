package com.nbm.carrental.exception;

public class UserAlreadyExistingException extends Throwable {
    public UserAlreadyExistingException(String s) {
        super(s);
    }
    public UserAlreadyExistingException() {
        super("User Already existing");
    }
}
