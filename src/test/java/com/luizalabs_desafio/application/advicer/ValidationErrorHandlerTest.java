package com.luizalabs_desafio.application.advicer;

import com.luizalabs_desafio.core.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidationErrorHandlerTest {

    @Mock
    private MessageSource messageSource;

    private ValidationErrorHandler validationErrorHandler;

    @BeforeEach
    void setUp() {
        validationErrorHandler = new ValidationErrorHandler(messageSource);
    }

    @Test
    void testHandleValidationError_MethodArgumentNotValidException() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getGlobalErrors()).thenReturn(List.of(new ObjectError("test", "Erro simulado")));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);
        ValidationErrorsOutputDto response = validationErrorHandler.handleValidationError(exception);
        assertNotNull(response);
    }

    @Test
    void testHandleValidationError_BindException() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getGlobalErrors()).thenReturn(List.of(new ObjectError("test", "Erro simulado")));
        BindException exception = new BindException(bindingResult);
        ValidationErrorsOutputDto response = validationErrorHandler.handleValidationError(exception);
        assertNotNull(response);
    }

    @Test
    void testHandleValidationError_NoSuchElementException() {
        NoSuchElementException exception = new NoSuchElementException("Element not found");

        FieldErrorOutputDto response = validationErrorHandler.handleValidationError(exception);

        assertNotNull(response);
        assertEquals("Element not found", response.getMessage());
    }

    @Test
    void testHandleValidationError_InvalidFileFormatException() {
        InvalidFileFormatException exception = new InvalidFileFormatException("Invalid file format");

        FieldErrorOutputDto response = validationErrorHandler.handleValidationError(exception);

        assertNotNull(response);
        assertEquals("Invalid file format", response.getMessage());
    }

    @Test
    void testHandleValidationError_IllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Illegal argument");

        FieldErrorOutputDto response = validationErrorHandler.handleValidationError(exception);

        assertNotNull(response);
        assertEquals("Illegal argument", response.getMessage());
    }

    @Test
    void testHandleValidationError_ResponseStatusException() {
        ResponseStatusException exception = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status");

        FieldErrorOutputDto response = validationErrorHandler.handleValidationError(exception);

        assertNotNull(response);
        assertEquals("400 BAD_REQUEST \"Invalid status\"", response.getMessage());
    }

    @Test
    void testHandleValidationError_InvalidFormatDateException() {
        InvalidFormatDateException exception = new InvalidFormatDateException("Invalid date format");

        ResponseEntity<String> response = validationErrorHandler.handleInvalidDateFormatException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid date format", response.getBody());
    }

    @Test
    void testHandleValidationError_InvalidFormatNumberException() {
        InvalidFormatNumberException exception = new InvalidFormatNumberException("Invalid number format");

        ResponseEntity<String> response = validationErrorHandler.handleInvalidFormatNumberException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid number format", response.getBody());
    }

    @Test
    void testHandleValidationError_ListIsEmptyException() {
        ListIsEmptyException exception = new ListIsEmptyException("List is empty");

        ResponseEntity<String> response = validationErrorHandler.handleListIsEmptyException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("List is empty", response.getBody());
    }

    @Test
    void testHandleValidationError_CustomerNotFoundException() {
        CustomerNotFoundException exception = new CustomerNotFoundException("Customer not found");

        ResponseEntity<String> response = validationErrorHandler.handleUserNotFoundException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Customer not found", response.getBody());
    }
}
