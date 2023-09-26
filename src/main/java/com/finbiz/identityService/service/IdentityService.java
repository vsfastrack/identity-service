package com.finbiz.identityService.service;

import com.finbiz.identityService.builder.IdentityResponseMapper;
import com.finbiz.identityService.constants.ErrorCodeConstants;
import com.finbiz.identityService.constants.ErrorMsgConstants;
import com.finbiz.identityService.dto.LoginDTO;
import com.finbiz.identityService.dto.RegisterUserDTO;
import com.finbiz.identityService.dto.RoleDTO;
import com.finbiz.identityService.exception.BusinessException;
import com.finbiz.identityService.service.spec.KeycloakFacade;
import com.finbiz.transactionmanager.api.spec.model.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdentityService {

    private final KeycloakFacade keycloakFacade;
    private final IdentityResponseMapper identityResponseMapper;

    public UserLoginResponse login(final LoginDTO loginDTO){
        AccessTokenResponse accessTokenResponse = keycloakFacade.login(loginDTO);
        if(ObjectUtils.isEmpty(accessTokenResponse) || StringUtils.isEmpty(accessTokenResponse.getToken()))
            throw BusinessException.of(ErrorCodeConstants.CODE_UNEXPECTED_ERR ,
                    ErrorMsgConstants.MSG_LOGIN_FAILED_AFTER_REGISTRATION , "Auth api err" , null);
        return identityResponseMapper.mapToLoginApiSuccessResponse(accessTokenResponse.getToken());
    }
    public void register(final RegisterUserDTO registerUserDTO){
        if(keycloakFacade.register(registerUserDTO) != 201)
            throw BusinessException.of(500 , "Technical Error",
                    "Something went wrong please try again later !!", null);
    }
    public void addRoles(final RoleDTO roleDTO , String username){
        keycloakFacade.addRoles(roleDTO , username);
    }
}
