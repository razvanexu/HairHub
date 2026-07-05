package com.hairhub.backend.service.validators;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PhoneValidationTest {
    private final PhoneValidation phoneValidation = new PhoneValidation();

    @Test
    public void validate_withVvalidPhoneNumber_doesNotThrowException() {
        assertDoesNotThrow(()->phoneValidation.validate("0724254578"));
    }

    @Test
    public void Validate_withNulPhoneNumber_ThrowsException() {
        assertThrows(ValidationException.class, ()->phoneValidation.validate(null));
    }

    @Test
    public void validate_withLettersInNumber_throwsException(){
        assertThrows(ValidationException.class, ()->phoneValidation.validate("0724254a78"));
    }

    @Test
    public void validate_withInvalidPrefix_throwsException(){
        assertThrows(ValidationException.class, ()->phoneValidation.validate("4"));
    }

    @Test
    public void validate_withSmallerCharNumber_throwsException(){
        assertThrows(ValidationException.class, ()->phoneValidation.validate("072425458"));
    }

    @Test
    public void validate_withLargeCharNumber_throwsException(){
        assertThrows(ValidationException.class, ()->phoneValidation.validate("07242545778"));
    }

    @Test
    public void validate_withEmptyString_throwsException(){
        assertThrows(ValidationException.class, ()->phoneValidation.validate(""));
    }
}
