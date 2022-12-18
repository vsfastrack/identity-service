package com.finbiz.identityService.validator;

import com.finbiz.identityService.dto.ErrorNotification;
import com.finbiz.transactionmanager.api.spec.model.LoginRequest;
import org.springframework.stereotype.Component;

@Component
public class LoginRequestValidator extends GenericRequestValidator{

    public void validateLoginRequest(LoginRequest loginRequest){
        validateUsername(loginRequest.getUsername());
    }
    public void validateLoginRequest(LoginRequest loginRequest , ErrorNotification errorNotification){
        validateUsername(loginRequest.getUsername());
    }
}
