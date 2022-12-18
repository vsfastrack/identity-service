package com.finbiz.identityService.constants;

public class ApiConstants {
    public static final String FIELD_USER_NAME = "userName";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_FIRST_NAME = "firstName";
    public static final String FIELD_LAST_NAME = "lastName";
    public static final String FIELD_EMAIL = "email";
    public static final String KEYCLOAK_CONFIG_SECRET = "secret";
    public static final String KEYCLOAK_CONFIG_CRANT_TYPE = "grant_type";
    public static final String KEYCLOAK_CONFIG_FIELD_PASSWORD = "password";
    public static final Integer CONSTRAINT_MIN_LENGTH_FIELD_USERNAME=40;
    public static final Integer CONSTRAINT_MIN_LENGTH_FIELD_PASSWORD=6;
    public static final Integer CONSTRAINT_MAX_LENGTH_FIELD_PASSWORD=15;
    public static final Integer KEYCLOAK_USER_CREATED_STATUS_CODE=201;
    public static final String STATUS_OK = "OK";
    public static final String STATUS_FAILURE = "FAILURE";
    public static final String X_TRANSACTION_ID = "x-transactionId";
    public static final String VERIFICATION_CHANNEL_SMS= "sms";
    public static final String VERIFICATION_OTP_APPROVED= "approved";
}




