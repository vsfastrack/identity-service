package com.finbiz.identityService.builder;

import com.finbiz.identityService.constants.ApiConstants;
import com.finbiz.identityService.dto.Error;
import com.finbiz.transactionmanager.api.spec.model.*;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IdentityResponseMapper {

    @Autowired
    private HttpServletRequest request;


    private void populateTransactionDetails(BaseApiResponse baseApiResponse){
        baseApiResponse.setTimestamp(LocalDateTime.now(ZoneOffset.UTC).toString());
        baseApiResponse.setStatus(!CollectionUtils.isEmpty(baseApiResponse.getErrors()) ?
                BaseApiResponse.StatusEnum.ERROR : BaseApiResponse.StatusEnum.OK);
        baseApiResponse.setContextId((String) request.getAttribute(ApiConstants.X_TRANSACTION_ID));
    }

    public VerificationApiResponse mapToSendVerificationTokenApiErrResponse(List<Error> errors , Integer statusCode){
        VerificationApiResponse registerApiResponse = new VerificationApiResponse();
        registerApiResponse.setErrors(errors.stream().map(Error::from).collect(Collectors.toList()));
        populateTransactionDetails(registerApiResponse);
        registerApiResponse.setStatusCode(statusCode);
        return registerApiResponse;
    }
    public VerificationApiResponse mapToSendVerificationTokenApiSuccessResponse(String verificationReferenceId){
        VerificationApiResponse registerApiResponse = new VerificationApiResponse();
        registerApiResponse.setVerificationRefId(verificationReferenceId);
        registerApiResponse.setTokenExpirryTime(10);
        registerApiResponse.setStatusCode(200);
        populateTransactionDetails(registerApiResponse);
        return registerApiResponse;
    }
    public VerificationCheckApiResponse mapToRegisterOtpValidationApiErrResponse(List<Error> errors , Integer statusCode){
        VerificationCheckApiResponse verificationCheckApiResponse = new VerificationCheckApiResponse();
        verificationCheckApiResponse.setErrors(errors.stream().map(Error::from).collect(Collectors.toList()));
        populateTransactionDetails(verificationCheckApiResponse);
        verificationCheckApiResponse.setStatusCode(statusCode);
        return verificationCheckApiResponse;
    }

    public VerificationCheckApiResponse mapToRegisterOtpValidationApiSuccessResponse(Integer statusCode){
        VerificationCheckApiResponse verificationCheckApiResponse = new VerificationCheckApiResponse();
        populateTransactionDetails(verificationCheckApiResponse);
        verificationCheckApiResponse.setStatusCode(statusCode);
        return verificationCheckApiResponse;
    }
    public UserLoginResponse mapToRegisterApiErrResponse(List<Error> errors , Integer statusCode){
        UserLoginResponse registerApiResponse = new UserLoginResponse();
        registerApiResponse.setErrors(errors.stream().map(Error::from).collect(Collectors.toList()));
        populateTransactionDetails(registerApiResponse);
        registerApiResponse.setStatusCode(statusCode);
        return registerApiResponse;
    }
    public UserLoginResponse mapToRegisterApiSuccessResponse(String accessToken){
        UserLoginResponse registerApiResponse = new UserLoginResponse();
        registerApiResponse.data(accessToken);
        populateTransactionDetails(registerApiResponse);
        registerApiResponse.setStatusCode(200);
        return registerApiResponse;
    }
    public UserLoginResponse mapToLoginApiErrResponse(List<Error> errors , Integer statusCode){
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setErrors(errors.stream().map(Error::from).collect(Collectors.toList()));
        populateTransactionDetails(userLoginResponse);
        userLoginResponse.setStatusCode(statusCode);
        return userLoginResponse;
    }
    public UserLoginResponse mapToLoginApiSuccessResponse(String accessToken){
        UserLoginResponse loginApiResponse = new UserLoginResponse();
        loginApiResponse.data(accessToken);
        populateTransactionDetails(loginApiResponse);
        loginApiResponse.setStatusCode(200);
        return loginApiResponse;
    }
}
