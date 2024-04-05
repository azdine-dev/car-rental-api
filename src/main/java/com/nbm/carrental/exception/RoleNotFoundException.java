package com.nbm.carrental.exception;

public class RoleNotFoundException extends Exception{
    public RoleNotFoundException(String message) {
        super(message);
    }
    public RoleNotFoundException() {
        super("Role Not Found");
    }
}
