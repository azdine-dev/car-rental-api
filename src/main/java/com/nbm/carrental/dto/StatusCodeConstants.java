package com.nbm.carrental.dto;

public class StatusCodeConstants {
    public static final int SUCCESS = 200;
    public static final int CREATED = 201;
    public static final int NO_CONTENT = 204;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int USER_NOT_ENABLED = 4000;
    public static final int EMAIL_ALREADY_EXIST = 4001 ;
    public static final int VERIFICATION_TOKEN_UNFOUND =4002 ;
    public static final int VERIFICATION_TOKEN_EXPIRED = 4003 ;
    public static final int USER_ALREADY_VERIFIED = 4004;
    public static final int INVALID_EMAIL_PASSWD = 4005;
    public static final int SOMETHING_WENT_WRONG = 4006;
    public static final int AGENCY_ALREADY_EXISTED =4007 ;
    public static final int SUCCESS_EMAIL_VERIFICATION =4008 ;

    // Add more status codes as needed
}