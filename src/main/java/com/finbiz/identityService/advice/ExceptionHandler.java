//package com.finbiz.identityService.advice;
//
//import com.finbiz.identityService.builder.ResponseBuilder;
//import com.finbiz.identityService.dto.ApiResponseDTO;
//import com.finbiz.identityService.dto.ExceptionDTO;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//
//@ControllerAdvice
//@Log4j2
//public class ExceptionHandler {
//
//    @org.springframework.web.bind.annotation.ExceptionHandler(ExceptionDTO.class)
//    public ResponseEntity<ApiResponseDTO> handleApiError(ExceptionDTO exceptionDTO) {
//        log.error("Error occured in api with code {} and cause {}",exceptionDTO.getCode() , exceptionDTO.getMessage());
//        return .buildApiErrResponse(exceptionDTO);
//    }
//}
