package com.finbiz.identityService.constants;

public class ErrorMsgConstants {
    public static final String UNEXPECTED_ERR_MSG="Unexpected error occured";
    public static final String EMPTY_REQUEST_BODY = "Request body cannot be empty.";
    public static final String EMPTY_FIELD = "Field cannot be empty.";
    public static final String MSG_VALIDATION_FAILURE_EMPTY_NAME="Name cannot be empty";
    public static final String MSG_VALIDATION_FAILURE_EMPTY_EMAIL="Email cannot be empty";
    public static final String MSG_VALIDATION_FAILURE_EMPTY_PHONE="Phone Number cannot be empty";
    public static final String MSG_VALIDATION_FAILURE_EMPTY_OTP_CODE="OTP Code cannot be empty";
    public static final String MSG_VALIDATION_FAILURE_EMPTY_VERIFICATION_REFERENCE_ID="Verification Reference Id cannot be empty";
    public static final String MSG_VALIDATION_FAILURE_NAME_TOO_LONG="Name cannot be greater than 40 characters";
    public static final String MSG_VALIDATION_FAILURE_EMAIL_TOO_LONG="Email cannot be greater than 40 characters";
    public static final String MSG_VALIDATION_FAILURE_PHONE_NUMBER_TOO_LONG="Phone number cannot be greater than 10 characters";
    public static final String MSG_VALIDATION_FAILURE_OTP_CODE_INVALID_LENGTH="OTP Code should be of 4 digits";
    public static final String MSG_VALIDATION_FAILURE_EMAIL_DOES_NOT_MATCH_PATTERN="Email does not match required pattern";
    public static final String MSG_VALIDATION_FAILURE_PHONE_DOES_NOT_MATCH_PATTERN="Phone number does not match required pattern";
    public static final String BAD_REQUEST = "Bad Request";
    public static final String MSG_VERIFY_API_RESPONDED_UNEXPECTEDLY = "Unexpected response from verify api";
    public static final String MSG_OTP_RESOURCE_NOT_FOUND = "Otp resource not found";
    public static final String MSG_OTP_RESOURCE_INVALID = "Invalid Otp";
    public static final String MSG_ENCRYPTION_COMPONENT_FAILED = "Encryption component failed";
    public static final String MSG_OTP_INVALID = "Invalid Otp";
    public static final String MSG_LOGIN_FAILED_AFTER_REGISTRATION = "Login failed during registration";

}

