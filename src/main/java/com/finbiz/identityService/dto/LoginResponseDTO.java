package com.finbiz.identityService.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;
    private Long expiresIn;
    private LocalDateTime issuedAt;
    public static LoginResponseDTO of(final String token){
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setToken(token);
        loginResponseDTO.setIssuedAt(LocalDateTime.now());
        return loginResponseDTO;
    }
    public static LoginResponseDTO of(final AccessTokenResponse accessTokenResponse){
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setToken(accessTokenResponse.getToken());
        loginResponseDTO.setExpiresIn(accessTokenResponse.getExpiresIn());
        loginResponseDTO.setIssuedAt(LocalDateTime.now());
        return loginResponseDTO;
    }
}
