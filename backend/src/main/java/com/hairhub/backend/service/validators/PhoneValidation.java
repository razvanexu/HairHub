package com.hairhub.backend.service.validators;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class PhoneValidation {
    private static final String PHONE_REGEX = "^07\\d{8}$";

    public void validate(String phone) {
        if (phone == null || !phone.matches(PHONE_REGEX)) {
            throw new ValidationException("Phone must be a valid Romanian mobile number (07xxxxxxxx)");
        }
    }
}
