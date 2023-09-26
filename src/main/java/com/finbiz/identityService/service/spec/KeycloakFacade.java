package com.finbiz.identityService.service.spec;

import com.finbiz.identityService.dto.LoginDTO;
import com.finbiz.identityService.dto.RegisterUserDTO;
import com.finbiz.identityService.dto.RoleDTO;
import com.finbiz.transactionmanager.api.spec.model.LoginRequest;
import com.finbiz.transactionmanager.api.spec.model.RegisterUserRequest;
import org.keycloak.representations.AccessTokenResponse;

public interface KeycloakFacade {
    AccessTokenResponse login(LoginDTO loginDTO);
    int register(RegisterUserDTO registerUserDTO);
    Boolean ifUserExists(String username);
    void addRoles(RoleDTO roleDTO , String username);
}
