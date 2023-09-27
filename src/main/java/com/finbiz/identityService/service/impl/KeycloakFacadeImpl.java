package com.finbiz.identityService.service.impl;

import com.finbiz.identityService.config.KeycloakConfig;
import com.finbiz.identityService.constants.ApiConstants;
import com.finbiz.identityService.dto.LoginDTO;
import com.finbiz.identityService.dto.RegisterUserDTO;
import com.finbiz.identityService.dto.RoleDTO;
import com.finbiz.identityService.service.spec.KeycloakFacade;
import com.google.common.collect.ImmutableMap;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Log4j2
public class KeycloakFacadeImpl implements KeycloakFacade {

    @Autowired
    private KeycloakConfig keycloakConfig;

    @Override
    public AccessTokenResponse login(LoginDTO loginDTO) {
        AuthzClient keyCloakClient = AuthzClient.create(
                new Configuration(keycloakConfig.getAuthServerUrl(),
                        keycloakConfig.getRealm(), keycloakConfig.getClientId(),
                        ImmutableMap.of(
                                ApiConstants.KEYCLOAK_CONFIG_SECRET , keycloakConfig.getClientSecret(),
                                ApiConstants.KEYCLOAK_CONFIG_CRANT_TYPE , ApiConstants.KEYCLOAK_CONFIG_FIELD_PASSWORD
                        ), null));
        return keyCloakClient.obtainAccessToken(
                loginDTO.getUsername(),
                loginDTO.getPassword());
    }
    @Override
    public int register(RegisterUserDTO registerUserDTO) {
        try (Keycloak keycloakClient = KeycloakBuilder.builder().serverUrl(keycloakConfig.getAuthServerUrl())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(keycloakConfig.getRealm())
                .clientId(keycloakConfig.getClientId())
                .username(keycloakConfig.getRootUser())
                .password(keycloakConfig.getRootPassword())
                .clientSecret(keycloakConfig.getClientSecret())
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build()) {

            keycloakClient.tokenManager().getAccessToken();
            UserRepresentation userRepresentation = createKeyCloakUser(registerUserDTO);
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(registerUserDTO.getPassword());
            userRepresentation.setCredentials(Collections.singletonList(passwordCred));
            RealmResource realmResource = keycloakClient.realm(keycloakConfig.getRealm());
            Response keycloakResponse = realmResource.users().create(userRepresentation);
            if(keycloakResponse.getStatus() == Response.Status.CREATED.getStatusCode()){
                String userId = keycloakResponse.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                List<String> roleNames = Collections.singletonList("ROLE_USER");
                for(String roleName:roleNames) {
                    RoleRepresentation roleRepresentation = realmResource.roles()
                                                            .get(roleName).toRepresentation();
                    realmResource.users().get(userId).roles().realmLevel().add(Collections.singletonList(roleRepresentation));
                }
                return keycloakResponse.getStatus();
            }else{
                return -1;
            }
        }catch (Exception exception){
            log.error("Error occurred while registering user with cause {}",ExceptionUtils.getMessage(exception));
            return -1;
        }
    }

    @Override
    public Boolean ifUserExists(String username) {
        RealmResource realmResource;
        try (Keycloak keycloakClient = KeycloakBuilder.builder().serverUrl(keycloakConfig.getAuthServerUrl())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(keycloakConfig.getRealm())
                .clientId(keycloakConfig.getClientId())
                .username(keycloakConfig.getRootUser())
                .password(keycloakConfig.getRootPassword())
                .clientSecret(keycloakConfig.getClientSecret())
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build()) {
            keycloakClient.tokenManager().getAccessToken();
            realmResource = keycloakClient.realm(keycloakConfig.getRealm());
            UsersResource usersResource = realmResource.users();
            return validateIfUserExists(username , usersResource);
        }catch (Exception exception){
            log.error("Error occurred while searching username {}",ExceptionUtils.getMessage(exception));
            return null;
        }
    }

    private Boolean validateIfUserExists(String username , UsersResource usersResource){
        List<UserRepresentation> userRepresentationList = usersResource.search(username);
        return !CollectionUtils.isEmpty(userRepresentationList);
    }

    private UserRepresentation createKeyCloakUser(final RegisterUserDTO registerUserDTO){
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(registerUserDTO.getEmail());
        userRepresentation.setEmail(registerUserDTO.getEmail());
        userRepresentation.setFirstName(registerUserDTO.getFirstName());
        userRepresentation.setLastName(registerUserDTO.getLastName());
        userRepresentation.setCreatedTimestamp(System.currentTimeMillis());
        userRepresentation.setEnabled(true);
        return userRepresentation;
    }

    @Override
    public void addRoles(RoleDTO roleDTO , String username) {
            Keycloak keycloakClient = KeycloakBuilder.builder().serverUrl(keycloakConfig.getAuthServerUrl())
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .realm(keycloakConfig.getRealm())
                    .clientId(keycloakConfig.getClientId())
                    .username(keycloakConfig.getRootUser())
                    .password(keycloakConfig.getRootPassword())
                    .clientSecret(keycloakConfig.getClientSecret())
                    .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();
            keycloakClient.tokenManager().getAccessToken();
            RealmResource realmResource = keycloakClient.realm(keycloakConfig.getRealm());
            UserRepresentation user = realmResource.users().search(username).get(0);
            roleDTO.getRoleNames().forEach(roleName -> {
                RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
                realmResource.users().get(user.getId()).roles().realmLevel().add(Arrays.asList(role));
            });
    }
}
