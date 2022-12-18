package com.finbiz.identityService.exception;

import com.finbiz.identityService.constants.ErrorCodeConstants;
import com.finbiz.identityService.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;

public class ExceptionBuilder {
    public static ExceptionDTO buildMissingFieldException(){
        return ExceptionDTO.builder().code(ErrorCodeConstants.CODE_MISSING_FIELD).httpStatus(HttpStatus.BAD_REQUEST).
                message("Missing {$}").description("{$} must not be null").build();
    }
    public static ExceptionDTO buildInvalidFieldException(){
        return ExceptionDTO.builder().code(ErrorCodeConstants.CODE_INVALID_FIELD).httpStatus(HttpStatus.BAD_REQUEST).
                message("Invalid {$}").description("{$} must match").build();
    }
    public static ExceptionDTO buildAuthFailedException(){
        return ExceptionDTO.builder().code(ErrorCodeConstants.CODE_AUTH_FAILED).
                httpStatus(HttpStatus.UNAUTHORIZED).
                message("Invalid {$}").description("Either of {$} is incorrect").build();
    }
    public static ExceptionDTO buildRegistrationException(){
        return ExceptionDTO.builder().code(ErrorCodeConstants.CODE_REGISTER_FAILED).
                httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).
                message("Registration failed").description("Authentication service responded unexpectedly !!").build();
    }

    public static void replcePlaceholdersWithValues(ExceptionDTO exceptionDTO , String fieldName){
        String replacedMsg = exceptionDTO.getMessage().replace("{$}" , fieldName);
        String replacedDescription = exceptionDTO.getDescription().replace("{$}" , fieldName);
        exceptionDTO.setMessage(replacedMsg);
        exceptionDTO.setDescription(replacedDescription);
    }
}
