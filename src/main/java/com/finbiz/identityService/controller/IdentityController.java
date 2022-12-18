package com.finbiz.identityService.controller;

import com.finbiz.identityService.builder.ResponseBuilder;
import com.finbiz.identityService.constants.LogConstants;
import com.finbiz.identityService.service.spec.IdentityFacade;
import com.finbiz.identityService.util.EncryptionUtil;
import com.finbiz.identityService.validator.LoginRequestValidator;
import com.finbiz.identityService.validator.RegisterUserRequestValidator;
import com.finbiz.transactionmanager.api.spec.handler.IdentityApi;
import com.finbiz.transactionmanager.api.spec.model.*;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Log4j2
public class IdentityController implements IdentityApi {

    @Autowired
    private LoginRequestValidator loginRequestValidator;
    @Autowired
    private RegisterUserRequestValidator registerUserRequestValidator;
    @Autowired
    private ResponseBuilder responseBuilder;
    @Autowired
    private IdentityFacade identityFacade;
    @Autowired
    private EncryptionUtil encryptionUtil;



    @Override
    public ResponseEntity<VerificationApiResponse> registrationVerificationOp(String phoneNumber) {
        log.info("Entering registrationVerificationOp() of IdentityController");
        VerificationApiResponse verificationApiResponse = identityFacade.sendVerificationToken(phoneNumber);
        log.info("End registrationVerificationOp() of IdentityController");
        return responseBuilder.buildApiResponse(verificationApiResponse);
    }

    @Override
    public ResponseEntity<VerificationCheckApiResponse> validateOTP(ValidateRegisterOTPRequest validateOTPRequest) {
        log.info("Entering validateOTP() of IdentityController");
        VerificationCheckApiResponse verificationApiResponse = identityFacade.validateRegistrationOtp(validateOTPRequest);
        log.info("End validateOTP() of IdentityController");
        return responseBuilder.buildApiResponse(verificationApiResponse);
    }



    @Override
    public ResponseEntity<UserLoginResponse> userRegistration(String referenceId, RegisterUserRequest registerUserRequest) {
        log.info("Entering userRegistration() for IdentityController with request {}", new Gson().toJson(registerUserRequest));
        UserLoginResponse registerApiResponse = identityFacade.register(registerUserRequest , referenceId);
        //log.info("Exiting userLoginOperation() for IdentityController with response {}", new Gson().toJson(registerApiResponse));
        return responseBuilder.buildApiResponse(registerApiResponse);
    }

    @Override
    public ResponseEntity<UserLoginResponse> userLoginOperation(String referenceId, String phoneNumber) {
        log.info(LogConstants.INFO_MSG_ENTRY , "userLoginOperation" , "IdentityController");
        UserLoginResponse userLoginResponse = identityFacade.login(phoneNumber , referenceId);
        log.info(LogConstants.INFO_MSG_EXIT , "userLoginOperation" , "IdentityController");
        return responseBuilder.buildApiResponse(userLoginResponse);
    }

    @Override
    public ResponseEntity<Void> logout(String userId) {
        return null;
    }

}
