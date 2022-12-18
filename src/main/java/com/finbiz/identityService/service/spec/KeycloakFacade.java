package com.finbiz.identityService.service.spec;

import com.finbiz.transactionmanager.api.spec.model.LoginRequest;
import com.finbiz.transactionmanager.api.spec.model.RegisterUserRequest;
import org.keycloak.representations.AccessTokenResponse;

public interface KeycloakFacade {
    AccessTokenResponse generateAccessToken(LoginRequest loginRequest);
    AccessTokenResponse generateAccessToken(String phoneNumber);
    AccessTokenResponse generateAccessToken(RegisterUserRequest registerUserRequest);
    String create(RegisterUserRequest registerUserRequest);

    Boolean ifUserExists(String username);
}
