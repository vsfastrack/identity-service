package com.finbiz.identityService.service.spec;

public interface VerificationFacade {
    String sendVerificationCode(String destination , String channel);
    Boolean validateVerificationCode(String code ,String destination , String channel);
}
