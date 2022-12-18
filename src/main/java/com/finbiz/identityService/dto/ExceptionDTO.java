package com.finbiz.identityService.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ExceptionDTO extends RuntimeException{
    private int code;
    private String message;
    private String description;
    private HttpStatus httpStatus;
}
