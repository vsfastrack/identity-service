package com.finbiz.identityService.service.impl;

import com.finbiz.identityService.constants.ApiConstants;
import com.finbiz.identityService.service.spec.VerificationFacade;
import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;

@Component
@Qualifier("twilioProvider")
public class TwilioVerifyImpl implements VerificationFacade {

    private final Logger LOG = LoggerFactory.getLogger(TwilioVerifyImpl.class);

    @Value("${verify.twilio.account.sid}")
    private String accountSID;
    @Value("${verify.twilio.account.auth.token}")
    private String authToken;
    @Value("${verify.twilio.verify.service.id}")
    private String serviceId;

    @Override
    public String sendVerificationCode(String destination, String channel) {
        try{
            Twilio.init(accountSID , authToken);
            Verification verification = Verification.creator(serviceId,destination,channel).create();
            return verification.getSid();
        }catch(Exception exception){
            LOG.error("Error occured during verification code sending with cause {}", ExceptionUtils.getMessage(exception));
        }
        return null;
    }

    @Override
    public Boolean validateVerificationCode(String code, String destination, String channel) {
        try{
            Twilio.init(accountSID , authToken);
            VerificationCheck verificationCheck = VerificationCheck.creator(serviceId)
                    .setTo(destination)
                    .setCode(code)
                    .create();
            return (!ObjectUtils.isEmpty(verificationCheck) &&
                    StringUtils.isNoneEmpty(verificationCheck.getStatus()) &&
                    ApiConstants.VERIFICATION_OTP_APPROVED.equals(verificationCheck.getStatus()));
        }catch(ApiException exception){
            LOG.error("Error occured during verification code validation with cause {}", ExceptionUtils.getMessage(exception));
            if(exception.getStatusCode() == 404){
                LOG.error("Error occured during verification code validation because verificationRecord is deleted");
            }
        }
        return false;
    }
}
