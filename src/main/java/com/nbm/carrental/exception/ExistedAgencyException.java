package com.nbm.carrental.exception;

public class ExistedAgencyException extends Exception {
    public ExistedAgencyException(String message){
        super(message);
    }
    public ExistedAgencyException(){
        super("Agency already existing");
    }

}
