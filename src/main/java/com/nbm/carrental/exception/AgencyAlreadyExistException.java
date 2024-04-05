package com.nbm.carrental.exception;

public class AgencyAlreadyExistException extends Exception {
    public AgencyAlreadyExistException(){
        super("Agency Already exist");
    }
}
