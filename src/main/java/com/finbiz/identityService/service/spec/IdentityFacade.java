package com.finbiz.identityService.service.spec;

import com.finbiz.identityService.dto.LoginDTO;
import com.finbiz.transactionmanager.api.spec.model.*;

public interface IdentityFacade {
    UserLoginResponse login(String phoneNumber , String verificationNumber);
    UserLoginResponse login(LoginDTO loginDTO);
    VerificationApiResponse sendVerificationToken(String phoneNumber);
    UserLoginResponse register(RegisterUserRequest signupRequest , String referenceId);
    VerificationCheckApiResponse validateRegistrationOtp(ValidateRegisterOTPRequest validateOtpRequest);

}
