package com.finbiz.identityService.resource;

import com.finbiz.identityService.dto.LoginDTO;
import com.finbiz.identityService.dto.RegisterUserDTO;
import com.finbiz.identityService.dto.RoleDTO;
import com.finbiz.identityService.service.IdentityService;
import com.finbiz.transactionmanager.api.spec.model.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class identityResource {

    private final IdentityService identityService;

    @PostMapping(value = "/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody LoginDTO loginDTO){
        UserLoginResponse loginResponse = identityService.login(loginDTO);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<HttpStatus> register(@RequestBody RegisterUserDTO registerUserDTO){
        identityService.register(registerUserDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PatchMapping(value = "/{username}/addRoles")
    public ResponseEntity<HttpStatus> addRoles(@RequestBody RoleDTO roleDTO ,
                                               @PathVariable("username") final String username){
        identityService.addRoles(roleDTO , username);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
