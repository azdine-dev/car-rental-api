package com.nbm.carrental.exception;

import com.nbm.carrental.dto.StatusCodeConstants;

public class VerificationTokenNotFound extends TokenException {

    public VerificationTokenNotFound(){
        super(StatusCodeConstants.VERIFICATION_TOKEN_UNFOUND,
                "Verification Token Not Found");
    }
}
