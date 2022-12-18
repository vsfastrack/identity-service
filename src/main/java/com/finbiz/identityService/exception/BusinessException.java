package com.finbiz.identityService.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessException extends RuntimeException{

    private Integer code;
    private String msg;
    private String details;
    private List<Error> errors;

    public static BusinessException of(Integer code , String message){
        return BusinessException.builder().code(code).msg(message).build();
    }
    public static BusinessException of(Integer code , String message , String details , List<Error> errors){
        return BusinessException.builder().code(code).msg(message).
                details(details).errors(errors).build();
    }
}
