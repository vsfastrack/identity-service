package com.finbiz.identityService.exception;

import com.finbiz.identityService.constants.ErrorCodeConstants;
import com.finbiz.identityService.dto.ExceptionDTO;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class ApiErrorRegistry {
    public static final Map<Integer, ExceptionDTO> errorRegistry = ImmutableMap.of(
            ErrorCodeConstants.CODE_MISSING_FIELD , ExceptionBuilder.buildMissingFieldException(),
            ErrorCodeConstants.CODE_INVALID_FIELD , ExceptionBuilder.buildInvalidFieldException(),
            ErrorCodeConstants.CODE_AUTH_FAILED , ExceptionBuilder.buildAuthFailedException()
    );
}
