package com.finbiz.identityService.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Data
@Configuration
public class KeycloakConfig {
    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${security.config.keycloak.client.secret}")
    private String clientSecret;
    @Value("${security.config.admin.root.user}")
    private String rootUser;
    @Value("${security.config.admin.root.password}")
    private String rootPassword;
}
