package com.nbm.carrental.exception;

public class UserNotEnabledException extends Exception{
    public UserNotEnabledException(String message) {
        super(message);
    }
    public UserNotEnabledException(){
        super("User Not verified");
    }
}
