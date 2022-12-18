package com.finbiz.identityService.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiResponseDTO<T> {
    private String timeStamp;
    private ApiErrDTO errorDetails;
    private T responseObj;
    private String status;
}
