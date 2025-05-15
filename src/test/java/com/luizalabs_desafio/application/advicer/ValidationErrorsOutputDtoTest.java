package com.luizalabs_desafio.application.advicer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationErrorsOutputDtoTest {

    private ValidationErrorsOutputDto validationErrors;

    @BeforeEach
    void setUp() {
        validationErrors = new ValidationErrorsOutputDto();
    }

    @Test
    void testAddGlobalErrorMessage() {
        validationErrors.addError("Global error 1");
        validationErrors.addError("Global error 2");

        assertEquals(2, validationErrors.getGlobalErrorMessages().size());
        assertTrue(validationErrors.getGlobalErrorMessages().contains("Global error 1"));
        assertTrue(validationErrors.getGlobalErrorMessages().contains("Global error 2"));
    }

    @Test
    void testAddFieldError() {
        validationErrors.addFieldError("username", "Username cannot be blank");
        validationErrors.addFieldError("password", "Password must be at least 8 characters");

        assertEquals(2, validationErrors.getErrors().size());
        assertEquals("username", validationErrors.getErrors().get(0).getField());
        assertEquals("Username cannot be blank", validationErrors.getErrors().get(0).getMessage());
        assertEquals("password", validationErrors.getErrors().get(1).getField());
        assertEquals("Password must be at least 8 characters", validationErrors.getErrors().get(1).getMessage());
    }

    @Test
    void testGetNumberOfErrors() {
        validationErrors.addError("Global error");
        validationErrors.addFieldError("email", "Email format is invalid");

        assertEquals(2, validationErrors.getNumberOfErrors());
    }
}
