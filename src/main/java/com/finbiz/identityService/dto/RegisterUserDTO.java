package com.finbiz.identityService.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String password;
    private Boolean isAuthor;
}
