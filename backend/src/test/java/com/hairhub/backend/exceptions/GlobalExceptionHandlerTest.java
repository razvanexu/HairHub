package com.hairhub.backend.exceptions;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {
    GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleEntityNotFoundException_returns404WithMessage() {
        //Given
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");

        //When
        ResponseEntity<String> response = handler.handleEntityNotFoundException(ex);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Entity not found", response.getBody());
    }

    @Test
    void handleIllegalArgumentException_returns400WithMessage() {
        //Given
        IllegalArgumentException ex = new IllegalArgumentException("Illegal Argument");

        //When
        ResponseEntity<String> response = handler.handleIllegalArgumentException(ex);

        //Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Illegal Argument", response.getBody());
    }

    @Test
    void handleValidationException_returns400WithMessage() {
        //Given
        ValidationException ex = new ValidationException("Incorrect argument");

        //When
        ResponseEntity<String> response = handler.handleValidationException(ex);

        //Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Incorrect argument", response.getBody());
    }
}
