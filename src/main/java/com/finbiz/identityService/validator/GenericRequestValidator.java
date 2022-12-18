package com.finbiz.identityService.validator;

import com.finbiz.identityService.constants.ApiConstants;
import com.finbiz.identityService.constants.ErrorCodeConstants;
import com.finbiz.identityService.constants.ErrorMsgConstants;
import com.finbiz.identityService.dto.Error;
import com.finbiz.identityService.dto.ErrorNotification;
import com.finbiz.identityService.dto.ExceptionDTO;
import com.finbiz.identityService.exception.ApiErrorRegistry;
import com.finbiz.identityService.exception.ExceptionBuilder;

import java.util.regex.Pattern;

public class GenericRequestValidator {

    private static final Pattern userNamePattern = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final Pattern passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    private static final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
    private static final Pattern phoneNumberPattern = Pattern.compile("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$");



    protected void validateUsername(String userName){
        if(Validators.validateEmptyField(userName))
            throwApiError(ApiConstants.FIELD_USER_NAME , ErrorCodeConstants.CODE_MISSING_FIELD);
        if(!Validators.validateIfPatternMatches(userName, emailPattern))
            throwApiError(ApiConstants.FIELD_USER_NAME ,ErrorCodeConstants.CODE_INVALID_FIELD);
        if(Validators.validateMaxLength(userName , ApiConstants.CONSTRAINT_MIN_LENGTH_FIELD_USERNAME))
            throwApiError(ApiConstants.FIELD_USER_NAME , ErrorCodeConstants.CODE_INVALID_FIELD);
    }

    protected void validatePassword(String password){
        if(Validators.validateEmptyField(password))
            throwApiError(ApiConstants.FIELD_PASSWORD , ErrorCodeConstants.CODE_MISSING_FIELD);
        if(Validators.validateMinLength(password , ApiConstants.CONSTRAINT_MIN_LENGTH_FIELD_PASSWORD))
            throwApiError(ApiConstants.FIELD_PASSWORD , ErrorCodeConstants.CODE_INVALID_FIELD);
        if(Validators.validateMaxLength(password , ApiConstants.CONSTRAINT_MAX_LENGTH_FIELD_PASSWORD))
            throwApiError(ApiConstants.FIELD_PASSWORD , ErrorCodeConstants.CODE_INVALID_FIELD);
//        if(!Validators.validateIfPatternMatches(password, passwordPattern))
//            throwApiError(ApiConstants.FIELD_PASSWORD ,ErrorCodeConstants.CODE_INVALID_FIELD);
    }

    protected void validateEmailAddress(String emailAddress , ErrorNotification errorNotification){
        if(Validators.validateEmptyField(emailAddress))
            errorNotification.addError(Error.of(ErrorCodeConstants.CODE_MISSING_FIELD ,
                    ErrorMsgConstants.MSG_VALIDATION_FAILURE_EMPTY_EMAIL , "Validation"));
        if(Validators.validateMaxLength(emailAddress , 40))
            errorNotification.addError(Error.of(ErrorCodeConstants.CODE_INVALID_FIELD ,
                    ErrorMsgConstants.MSG_VALIDATION_FAILURE_EMAIL_TOO_LONG , "Validation"));
        if(!Validators.validateIfPatternMatches(emailAddress, emailPattern))
            errorNotification.addError(Error.of(ErrorCodeConstants.CODE_INVALID_FIELD ,
                    ErrorMsgConstants.MSG_VALIDATION_FAILURE_EMAIL_DOES_NOT_MATCH_PATTERN , "Validation"));
    }

    protected void validatePhoneNumber(String phoneNumber , ErrorNotification errorNotification){
        if(Validators.validateEmptyField(phoneNumber))
            errorNotification.addError(Error.of(ErrorCodeConstants.CODE_MISSING_FIELD ,
                    ErrorMsgConstants.MSG_VALIDATION_FAILURE_EMPTY_PHONE , "Validation"));
        if(Validators.validateMaxLength(phoneNumber , 15))
            errorNotification.addError(Error.of(ErrorCodeConstants.CODE_INVALID_FIELD ,
                    ErrorMsgConstants.MSG_VALIDATION_FAILURE_PHONE_NUMBER_TOO_LONG , "Validation"));
        if(!Validators.validateIfPatternMatches(phoneNumber, phoneNumberPattern))
            errorNotification.addError(Error.of(ErrorCodeConstants.CODE_INVALID_FIELD ,
                    ErrorMsgConstants.MSG_VALIDATION_FAILURE_PHONE_DOES_NOT_MATCH_PATTERN , "Validation"));
    }

    protected void throwApiError(String fieldName , Integer errorCode){
        ExceptionDTO apiError = ApiErrorRegistry.errorRegistry.get(errorCode);
        ExceptionBuilder.replcePlaceholdersWithValues(apiError , fieldName);
        throw apiError;
    }
}
