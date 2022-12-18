package com.finbiz.identityService.service.impl;

import com.finbiz.identityService.builder.IdentityResponseMapper;
import com.finbiz.identityService.constants.ApiConstants;
import com.finbiz.identityService.constants.ErrorCodeConstants;
import com.finbiz.identityService.constants.ErrorMsgConstants;
import com.finbiz.identityService.domain.Verification;
import com.finbiz.identityService.dto.Error;
import com.finbiz.identityService.dto.ErrorNotification;
import com.finbiz.identityService.enums.VerificationStatusEnum;
import com.finbiz.identityService.exception.BusinessException;
import com.finbiz.identityService.repository.VerificationRepository;
import com.finbiz.identityService.service.spec.IdentityFacade;
import com.finbiz.identityService.service.spec.KeycloakFacade;
import com.finbiz.identityService.service.spec.VerificationFacade;
import com.finbiz.identityService.validator.RegisterUserRequestValidator;
import com.finbiz.transactionmanager.api.spec.model.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
@Log4j2
public class IdentityFacadeImpl implements IdentityFacade {

    @Autowired
    private KeycloakFacade keycloakFacade;
    @Autowired
    @Qualifier("twilioProvider")
    private VerificationFacade verificationFacade;
    @Autowired
    private VerificationRepository verificationRepository;
    @Autowired
    private IdentityResponseMapper identityResponseMapper;
    @Autowired
    private RegisterUserRequestValidator requestValidator;



    @Override
    public VerificationApiResponse sendVerificationToken(String phoneNumber) {
        ErrorNotification validationNotification = requestValidator.validateRegistrationVerificationRequest(phoneNumber);
        if(validationNotification.hasErrors())
            return identityResponseMapper.mapToSendVerificationTokenApiErrResponse(validationNotification.getErrors() , 400);
        String verificationReferenceId = handleVerification(phoneNumber);
        return identityResponseMapper.mapToSendVerificationTokenApiSuccessResponse(verificationReferenceId);
    }

    @Override
    public VerificationCheckApiResponse validateRegistrationOtp(ValidateRegisterOTPRequest validateOtpRequest) {
        ErrorNotification validatioErrnNotification = requestValidator.validateRegistrationOtp(validateOtpRequest);
        if(validatioErrnNotification.hasErrors())
            return identityResponseMapper.mapToRegisterOtpValidationApiErrResponse(validatioErrnNotification.getErrors() , 400);

        ErrorNotification verificationResourceNotification = checkIfVerificationResourceExists(validateOtpRequest.getReferenceId(),
                validateOtpRequest.getPhoneNumber() , VerificationStatusEnum.PENDING);
        if(verificationResourceNotification.hasErrors())
            return identityResponseMapper.mapToRegisterOtpValidationApiErrResponse(verificationResourceNotification.getErrors() , 404);

        ErrorNotification validateeOtpErrorNotification = ErrorNotification.getInstance();
        handleOtpVerification(validateOtpRequest , validateeOtpErrorNotification);

        if(validateeOtpErrorNotification.hasErrors())
            return identityResponseMapper.mapToRegisterOtpValidationApiErrResponse(validateeOtpErrorNotification.getErrors() , 404);
        verificationRepository.upateVerificationStatus(VerificationStatusEnum.APPROVED , validateOtpRequest.getReferenceId());
        Integer statusCode = keycloakFacade.ifUserExists(validateOtpRequest.getPhoneNumber()) ? 409 : 204;
        return identityResponseMapper.mapToRegisterOtpValidationApiSuccessResponse(statusCode);
    }
    @Override
    public UserLoginResponse register(RegisterUserRequest registerUserRequest , String referenceId) {
        ErrorNotification validationNotification = ErrorNotification.getInstance();
            requestValidator.validateRegisterUserRequest(registerUserRequest);
        if(validationNotification.hasErrors())
            return identityResponseMapper.mapToRegisterApiErrResponse(validationNotification.getErrors() , 400);
        ErrorNotification verificationResourceErrNotification = checkIfVerificationResourceExists(referenceId,
                registerUserRequest.getMobileNumber() , VerificationStatusEnum.APPROVED);
        if(verificationResourceErrNotification.hasErrors())
            return identityResponseMapper.mapToRegisterApiErrResponse(
                    verificationResourceErrNotification.getErrors() , 404);
        ErrorNotification registrationNotification = ErrorNotification.getInstance();
        String userId = handleRegistration(registerUserRequest , registrationNotification);
        if(StringUtils.isEmpty(userId))
            return identityResponseMapper.mapToRegisterApiErrResponse(registrationNotification.getErrors() , 500);
        verificationRepository.deleteVerificationByClaimedResource(registerUserRequest.getMobileNumber());
        AccessTokenResponse accessTokenResponse = keycloakFacade.generateAccessToken(registerUserRequest);
        if(ObjectUtils.isEmpty(accessTokenResponse) || StringUtils.isEmpty(accessTokenResponse.getToken()))
            throw BusinessException.of(ErrorCodeConstants.CODE_UNEXPECTED_ERR ,
                    ErrorMsgConstants.MSG_LOGIN_FAILED_AFTER_REGISTRATION , "Auth api err" , null);
        return identityResponseMapper.mapToRegisterApiSuccessResponse(accessTokenResponse.getToken());
    }

    private String handleVerification(String mobileNumber){
        String verificationReferenceId = verificationFacade.sendVerificationCode(mobileNumber , ApiConstants.VERIFICATION_CHANNEL_SMS);
        if(StringUtils.isEmpty(verificationReferenceId))
            throw BusinessException.of(ErrorCodeConstants.CODE_THIRD_PARTY_UNEXPECTED_BEHAVUOUR ,
                    ErrorMsgConstants.MSG_VERIFY_API_RESPONDED_UNEXPECTEDLY , "Third party api err" , null);
        verificationRepository.save(Verification.of(verificationReferenceId ,mobileNumber , VerificationStatusEnum.PENDING));
        return verificationReferenceId;
    }

    private void handleOtpVerification(ValidateRegisterOTPRequest validateRegisterOTPRequest , ErrorNotification validateeErrorNotification){
        if(!verificationFacade.validateVerificationCode(validateRegisterOTPRequest.getOtpCode() ,
                validateRegisterOTPRequest.getPhoneNumber() , "sms"))
            validateeErrorNotification.addError(Error.of(
                    ErrorCodeConstants.CODE_THIRD_PARTY_SERVICE_RESPONSE_INCORRECT ,
                    ErrorMsgConstants.MSG_OTP_INVALID , "Business Error"));
    }

    private String handleRegistration(RegisterUserRequest registerUserRequest , ErrorNotification errorNotification){
        String accessToken = keycloakFacade.create(registerUserRequest);
        if(StringUtils.isEmpty(accessToken))
            errorNotification.addError(Error.of(ErrorCodeConstants.CODE_UNEXPECTED_ERR ,
                    ErrorMsgConstants.UNEXPECTED_ERR_MSG , "Technical Error"));
        return accessToken;
    }
    private ErrorNotification checkIfVerificationResourceExists(String referenceId, String phoneNumber , VerificationStatusEnum verificationStatusEnum){
        ErrorNotification errorNotification = ErrorNotification.getInstance();
        Long verificationCount = verificationRepository.countByVerificationReferenceAndClaimedResourceAndStatus(
                referenceId , phoneNumber ,verificationStatusEnum);
        if(verificationCount == 0)
            errorNotification.addError(Error.of(ErrorCodeConstants.CODE_INVALID_FIELD ,
                    ErrorMsgConstants.MSG_OTP_RESOURCE_NOT_FOUND , "Business Error"));
        return errorNotification;
    }

    @Override
    public UserLoginResponse login(String phoneNumber, String referenceId) {
        ErrorNotification verificationResourceErrNotification = checkIfVerificationResourceExists(referenceId ,phoneNumber , VerificationStatusEnum.APPROVED);
        if(verificationResourceErrNotification.hasErrors())
            return identityResponseMapper.mapToLoginApiErrResponse(
                    verificationResourceErrNotification.getErrors() , 404);
        AccessTokenResponse accessTokenResponse = keycloakFacade.generateAccessToken(phoneNumber);
        if(ObjectUtils.isEmpty(accessTokenResponse) || StringUtils.isEmpty(accessTokenResponse.getToken()))
            throw BusinessException.of(ErrorCodeConstants.CODE_UNEXPECTED_ERR ,
                    ErrorMsgConstants.MSG_LOGIN_FAILED_AFTER_REGISTRATION , "Auth api err" , null);
        verificationRepository.deleteVerificationByClaimedResource(phoneNumber);
        return identityResponseMapper.mapToLoginApiSuccessResponse(accessTokenResponse.getToken());
    }
}
