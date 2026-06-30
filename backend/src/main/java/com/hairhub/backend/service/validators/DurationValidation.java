package com.hairhub.backend.service.validators;

import org.springframework.stereotype.Component;

@Component
public class DurationValidation {
    public void validate(Integer duration){
        if(duration == null || duration %30 != 0){
            throw new IllegalArgumentException("Duration must be a multiple of 30 minutes");
        }
    }
}
