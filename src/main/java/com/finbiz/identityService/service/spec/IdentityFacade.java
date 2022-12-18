package com.finbiz.identityService.service.spec;

import com.finbiz.transactionmanager.api.spec.model.*;

public interface IdentityFacade {
    UserLoginResponse login(String phoneNumber , String verificationNumber);
    VerificationApiResponse sendVerificationToken(String phoneNumber);
    UserLoginResponse register(RegisterUserRequest signupRequest , String referenceId);
    VerificationCheckApiResponse validateRegistrationOtp(ValidateRegisterOTPRequest validateOtpRequest);

}
