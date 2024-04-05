package com.nbm.carrental.exception;

public class AgencyNotFoundException extends Exception {

    public AgencyNotFoundException(String message){
        super(message);
    }
    public AgencyNotFoundException(){
        super("Agency Not Found");
    }
}
