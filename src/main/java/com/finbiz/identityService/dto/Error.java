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

}
