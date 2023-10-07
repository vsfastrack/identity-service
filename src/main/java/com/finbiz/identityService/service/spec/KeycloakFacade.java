package com.finbiz.identityService.service.spec;

import com.finbiz.identityService.dto.LoginDTO;
import com.finbiz.identityService.dto.RegisterUserDTO;
import com.finbiz.identityService.dto.RoleDTO;
import org.keycloak.representations.AccessTokenResponse;

public interface KeycloakFacade {
    AccessTokenResponse login(LoginDTO loginDTO);
    int register(RegisterUserDTO registerUserDTO);
    Boolean ifUserExists(String username);
    void addRoles(RoleDTO roleDTO , String username);
}
