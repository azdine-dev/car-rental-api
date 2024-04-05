package com.nbm.carrental.exception;

import com.nbm.carrental.dto.StatusCodeConstants;

public class VerificationTokenExpired extends TokenException {

    public VerificationTokenExpired(){
        super(StatusCodeConstants.VERIFICATION_TOKEN_EXPIRED,
                "Verification Token Expired");
    }
}
