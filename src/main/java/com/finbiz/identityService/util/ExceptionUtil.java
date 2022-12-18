package com.finbiz.identityService.util;

import com.finbiz.identityService.constants.ErrorCodeConstants;
import com.finbiz.identityService.dto.ExceptionDTO;
import com.finbiz.identityService.exception.ApiErrorRegistry;
import com.finbiz.identityService.exception.ExceptionBuilder;
import org.keycloak.authorization.client.util.HttpResponseException;

public class ExceptionUtil {
    public static ExceptionDTO handleKeyCloakApiErr(Exception exception){
        if(exception instanceof HttpResponseException){
            HttpResponseException httpResponseException = (HttpResponseException) exception;
            if(httpResponseException.getStatusCode() == 401){
                ExceptionDTO apiError = ApiErrorRegistry.errorRegistry.get(ErrorCodeConstants.CODE_AUTH_FAILED);
                ExceptionBuilder.replcePlaceholdersWithValues(apiError , "username or password");
                return apiError;
            }
        }
        return null;
    }
}
