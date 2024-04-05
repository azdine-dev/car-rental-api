package com.nbm.carrental.exception;


public class InvalidEmailOrPassword extends Exception{
    public InvalidEmailOrPassword(String message) {
        super(message);
    }

    public InvalidEmailOrPassword(){
        super("Invalid Email Or Password");
    }
}