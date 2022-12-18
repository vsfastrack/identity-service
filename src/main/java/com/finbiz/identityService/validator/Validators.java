package com.finbiz.identityService.validator;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Validators {
    public static boolean validateEmptyField(String value){
        return StringUtils.isEmpty(value);
    }

    public static boolean validateNonEmptyField(OffsetDateTime offsetDateTime){
        return !offsetDateTime.format(DateTimeFormatter.ISO_DATE_TIME).isEmpty();
    }

    public static boolean validateIfPatternMatches(String value , Pattern pattern){
        return pattern.matcher(value).matches();
    }

    public static boolean validateMinLength(String value , int length){
        return (StringUtils.length(value) < length) ;
    }

    public static boolean validateMaxLength(String value , int length){
        return (StringUtils.length(value) > length) ;
    }

    public static boolean validateMinValue(BigDecimal value , BigDecimal minValue){
        return value.compareTo(minValue) >= 0;
    }
}

