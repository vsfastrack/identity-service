package com.finbiz.identityService.dto;

public class Error {
    private Integer code;
    private String message;
    private String type;

    public static Error of(Integer code , String message , String type){
        Error error = new Error();
        error.code = code;
        error.message = message;
        error.type = type;
        return error;
    }

    public static com.finbiz.transactionmanager.api.spec.model.Error from(Error error){
        com.finbiz.transactionmanager.api.spec.model.Error errorResponse = new com.finbiz.transactionmanager.api.spec.model.Error();
        errorResponse.setCode(error.code);
        errorResponse.setMessage(error.message);
        errorResponse.setDetails(error.type);
        return  errorResponse;
    }

}
