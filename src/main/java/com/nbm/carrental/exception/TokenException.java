package com.nbm.carrental.exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract  class TokenException extends Exception{
    private int statusCode;
    public TokenException(int statusCode, String message){
        super(message);
        this.statusCode = statusCode;
    }
}
