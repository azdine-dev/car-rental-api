package com.nbm.carrental.exception;

import com.nbm.carrental.dto.StatusCodeConstants;

public class UserAlreadyVerified extends TokenException {

    public UserAlreadyVerified(){
        super(StatusCodeConstants.USER_ALREADY_VERIFIED,
                "User Already Verified");
    }

}
