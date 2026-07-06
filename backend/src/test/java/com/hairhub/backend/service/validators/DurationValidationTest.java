package com.hairhub.backend.service.validators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DurationValidationTest {
    private final DurationValidation validator = new DurationValidation();

    @Test
    void validate_withMultipleOfThirty_doesNotThrow(){
        assertDoesNotThrow(() -> validator.validate(30));
    }

    @Test
    void validate_withNullDuration_throwsException(){
        assertThrows(IllegalArgumentException.class, () -> validator.validate(null));
    }

    @Test
    void validate_withNonMultipleOfThirty_throwsException(){
        assertThrows(IllegalArgumentException.class, () -> validator.validate(31));
    }

    @Test
    void validate_withNegativeDuration_throwsException(){
        assertThrows(IllegalArgumentException.class, () -> validator.validate(-30));
    }

    @Test
    void validate_withZeroDuration_throwsException(){
        assertThrows(IllegalArgumentException.class, () -> validator.validate(0));
    }
}
