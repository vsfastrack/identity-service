package com.finbiz.identityService.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;
    private LocalDateTime issuedAt;
    public static LoginResponseDTO of(final String token){
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setToken(token);
        loginResponseDTO.setIssuedAt(LocalDateTime.now());
        return loginResponseDTO;
    }
}
