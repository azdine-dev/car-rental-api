package com.nbm.carrental.exception;

public class SomethingWentWrongException extends Throwable {
    public SomethingWentWrongException(String message) {
        super(message);
    }
    public SomethingWentWrongException() {
        super("Something went Wrong");
    }
}
