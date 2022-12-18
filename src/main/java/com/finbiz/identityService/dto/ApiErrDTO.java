package com.finbiz.identityService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrDTO {
    private int code;
    private String message;
    private String description;
    private HttpStatus httpStatus;

    public static ApiErrDTO of(ExceptionDTO exceptionDTO){
        ApiErrDTO apiErrDTO = new ApiErrDTO();
        apiErrDTO.setCode(exceptionDTO.getCode());
        apiErrDTO.setMessage(exceptionDTO.getMessage());
        apiErrDTO.setDescription(exceptionDTO.getDescription());
        apiErrDTO.setHttpStatus(exceptionDTO.getHttpStatus());
        return apiErrDTO;
    }
}
