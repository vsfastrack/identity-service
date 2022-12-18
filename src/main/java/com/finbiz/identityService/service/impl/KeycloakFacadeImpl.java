package com.finbiz.identityService.service.impl;

import com.finbiz.identityService.constants.ApiConstants;
import com.finbiz.identityService.constants.ErrorCodeConstants;
import com.finbiz.identityService.constants.ErrorMsgConstants;
import com.finbiz.identityService.exception.BusinessException;
import com.finbiz.identityService.exception.ExceptionBuilder;
import com.finbiz.identityService.service.spec.KeycloakFacade;
import com.finbiz.identityService.util.EncryptionUtil;
import com.finbiz.transactionmanager.api.spec.model.LoginRequest;
import com.finbiz.transactionmanager.api.spec.model.RegisterUserRequest;
import com.google.common.collect.ImmutableMap;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class KeycloakFacadeImpl implements KeycloakFacade {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${security.config.admin.root.user}")
    private String rootUser;
    @Value("${security.config.admin.root.password}")
    private String rootPassword;
    @Autowired
    private EncryptionUtil encryptionUtil;

    private static final Map<String , Object> clientCredentials = ImmutableMap.of(
            ApiConstants.KEYCLOAK_CONFIG_SECRET , "okyVZn0J7dX9z7JpmMdl0wDpErDbcnzo",
            ApiConstants.KEYCLOAK_CONFIG_CRANT_TYPE , ApiConstants.KEYCLOAK_CONFIG_FIELD_PASSWORD
    );

    private Keycloak buildKeyCloakClient(){
        return KeycloakBuilder.builder().serverUrl(authServerUrl)
                .grantType(OAuth2Constants.PASSWORD).realm(realm).clientId(clientId)
                .username(rootUser).password(rootPassword).clientSecret("okyVZn0J7dX9z7JpmMdl0wDpErDbcnzo")
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();
    }

    @Override
    public AccessTokenResponse generateAccessToken(LoginRequest loginRequest) {
        AuthzClient keyCloakClient = AuthzClient.create(
                new Configuration(authServerUrl, realm, clientId,
                        clientCredentials, null));
            return keyCloakClient.obtainAccessToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword());
    }

    @Override
    public AccessTokenResponse generateAccessToken(String phoneNumber) {
        AuthzClient keyCloakClient = AuthzClient.create(
                new Configuration(authServerUrl, realm, clientId,
                        clientCredentials, null));
        return keyCloakClient.obtainAccessToken(phoneNumber,encryptionUtil.encrypt(phoneNumber));
    }

    public AccessTokenResponse generateAccessToken(RegisterUserRequest registerUserRequest) {
        AuthzClient keyCloakClient = AuthzClient.create(
                new Configuration(authServerUrl, realm, clientId,
                        clientCredentials, null));
        return keyCloakClient.obtainAccessToken(registerUserRequest.getMobileNumber() ,
                encryptionUtil.encrypt(registerUserRequest.getMobileNumber()));
    }

    @Override
    public String create(RegisterUserRequest registerUserRequest) {
        UserRepresentation userRepresentation;
        RealmResource realmResource;
        try (Keycloak keycloakClient = buildKeyCloakClient()) {
            keycloakClient.tokenManager().getAccessToken();
            userRepresentation = createKeyCloakUser(registerUserRequest);
            realmResource = keycloakClient.realm(realm);
            String userId = createUser(realmResource.users() , userRepresentation);
            UsersResource usersResource = realmResource.users();
            UserResource userResource = usersResource.get(userId);
            //Set Password for user
            String password = encryptionUtil.encrypt(registerUserRequest.getMobileNumber());
            if(StringUtils.isEmpty(password))
                throw BusinessException.of(ErrorCodeConstants.CODE_UNEXPECTED_ERR,
                        ErrorMsgConstants.MSG_ENCRYPTION_COMPONENT_FAILED);
            setPasswordCredentials(usersResource , userId , password);
            setUserRoles(realmResource , userResource , "USER");
            return userId;
        }catch (Exception exception){
            log.error("Error occured while regitering user with cause {}",ExceptionUtils.getMessage(exception));
            return null;
        }
    }

    @Override
    public Boolean ifUserExists(String username) {
        UserRepresentation userRepresentation;
        RealmResource realmResource;
        try (Keycloak keycloakClient = buildKeyCloakClient()) {
            keycloakClient.tokenManager().getAccessToken();
            realmResource = keycloakClient.realm(realm);
            UsersResource usersResource = realmResource.users();
            return validateIfUserExists(username , usersResource);
        }catch (Exception exception){
            log.error("Error occured while searching username {}",ExceptionUtils.getMessage(exception));
            return null;
        }
    }

    private String createUser(UsersResource usersResource , UserRepresentation userRepresentation){
            Response response = usersResource.create(userRepresentation);
            if(response.getStatus() != ApiConstants.KEYCLOAK_USER_CREATED_STATUS_CODE){
                log.error("Error occured while creating user  in keycloak layer {}", response.getStatus());
                throw ExceptionBuilder.buildRegistrationException();
            }
        return CreatedResponseUtil.getCreatedId(response);
    }

    private Boolean validateIfUserExists(String username , UsersResource usersResource){
        List<UserRepresentation> userRepresentationList = usersResource.search(username);
        return !CollectionUtils.isEmpty(userRepresentationList);
    }

    private UserRepresentation createKeyCloakUser(final RegisterUserRequest registerUserRequest){
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(registerUserRequest.getMobileNumber());
        userRepresentation.setEmail(registerUserRequest.getEmail());
        userRepresentation.setFirstName(registerUserRequest.getName());
        userRepresentation.setEnabled(true);
        return userRepresentation;
    }

    private void setPasswordCredentials(final UsersResource usersResource , final String userId ,final String password){
        try{
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(password);
            UserResource userResource = usersResource.get(userId);
            userResource.resetPassword(passwordCred);
        }catch(Exception exception){
            log.error("Error occured while setting  password creds in keycloak layer {}", exception.getMessage());
            throw ExceptionBuilder.buildRegistrationException();
        }
    }

    private void setUserRoles(RealmResource realmResource , UserResource userResource , String role){
        try{
            // Get realm role student
            RoleRepresentation realmRoleUser = realmResource.roles().get(role).toRepresentation();
            // Assign realm role student to user
            userResource.roles().realmLevel().add(Arrays.asList(realmRoleUser));
        }catch(Exception exception){
            log.error("Error occured while setting  roles in keycloak layer {}", exception.getMessage());
            throw ExceptionBuilder.buildRegistrationException();
        }
    }
}
