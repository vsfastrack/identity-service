package com.finbiz.identityService.dto;

import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorNotification {

    private List<Error> errors;
    public void addError(Error error){
        this.errors.add(error);
    }
    public boolean hasErrors(){
        return !CollectionUtils.isEmpty(this.errors);
    }

    private ErrorNotification(){}

    public static ErrorNotification getInstance(){
        ErrorNotification notification = new ErrorNotification();
        notification.init();
        return notification;
    }
    private void init(){
        this.errors = new ArrayList<>();
    }
}
