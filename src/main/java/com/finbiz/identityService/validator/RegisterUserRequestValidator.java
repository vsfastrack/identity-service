package com.finbiz.identityService.validator;

import com.finbiz.identityService.constants.ErrorCodeConstants;
import com.finbiz.identityService.constants.ErrorMsgConstants;
import com.finbiz.identityService.dto.Error;
import com.finbiz.identityService.dto.ErrorNotification;
import com.finbiz.transactionmanager.api.spec.model.RegisterUserRequest;
import com.finbiz.transactionmanager.api.spec.model.ValidateRegisterOTPRequest;
import org.springframework.stereotype.Component;

@Component
public class RegisterUserRequestValidator extends GenericRequestValidator{

    public ErrorNotification validateRegisterUserRequest(RegisterUserRequest registerUserRequest){
        ErrorNotification errorNotification = ErrorNotification.getInstance();
        validateName(registerUserRequest.getName() , errorNotification);
        validateEmailAddress(registerUserRequest.getEmail() , errorNotification);
        validatePhoneNumber(registerUserRequest.getMobileNumber(),  errorNotification);
        return errorNotification;
    }

    public ErrorNotification validateRegistrationVerificationRequest(String phoneNumber){
        ErrorNotification errorNotification = ErrorNotification.getInstance();
        validatePhoneNumber(phoneNumber , errorNotification);
        return errorNotification;
    }


    public ErrorNotification validateRegistrationOtp(ValidateRegisterOTPRequest registerOTPRequest){
        ErrorNotification errorNotification = ErrorNotification.getInstance();
        validatePhoneNumber(registerOTPRequest.getPhoneNumber() , errorNotification);
        validateOtpCode(registerOTPRequest.getOtpCode() , errorNotification);
        validateReferenceId(registerOTPRequest.getReferenceId() , errorNotification);
        return errorNotification;
    }

    private void validateName(final String name , ErrorNotification errorNotification){
        if(Validators.validateEmptyField(name))
            errorNotification.addError(Error.of(ErrorCodeConstants.CODE_MISSING_FIELD ,
                    ErrorMsgConstants.MSG_VALIDATION_FAILURE_EMPTY_NAME , "Validation"));
        if(Validators.validateMaxLength(name , 40))
            errorNotification.addError(Error.of(ErrorCodeConstants.CODE_INVALID_FIELD ,
                    ErrorMsgConstants.MSG_VALIDATION_FAILURE_NAME_TOO_LONG , "Validation"));
    }


    private void validateOtpCode(final String otpCode , ErrorNotification errorNotification){
        if(Validators.validateEmptyField(otpCode))
            errorNotification.addError(Error.of(ErrorCodeConstants.CODE_MISSING_FIELD ,
                    ErrorMsgConstants.MSG_VALIDATION_FAILURE_EMPTY_OTP_CODE , "Validation"));
        if(Validators.validateMaxLength(otpCode , 4) && Validators.validateMinLength(otpCode , 4))
            errorNotification.addError(Error.of(ErrorCodeConstants.CODE_INVALID_FIELD ,
                    ErrorMsgConstants.MSG_VALIDATION_FAILURE_OTP_CODE_INVALID_LENGTH , "Validation"));
    }

    private void validateReferenceId(final String verificationRefId , ErrorNotification errorNotification){
        if(Validators.validateEmptyField(verificationRefId))
            errorNotification.addError(Error.of(ErrorCodeConstants.CODE_MISSING_FIELD ,
                    ErrorMsgConstants.MSG_VALIDATION_FAILURE_EMPTY_VERIFICATION_REFERENCE_ID , "Validation"));
    }

}
