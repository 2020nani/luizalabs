package com.luizalabs_desafio.application.advicer;

import com.luizalabs_desafio.core.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class ValidationErrorHandler {

    private final MessageSource messageSource;

    public ValidationErrorHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorsOutputDto handleValidationError(MethodArgumentNotValidException exception) {

        List<ObjectError> globalErrors = exception.getBindingResult().getGlobalErrors();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        return buildValidationErrors(globalErrors,
                fieldErrors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ValidationErrorsOutputDto handleValidationError(BindException exception) {

        List<ObjectError> globalErrors = exception.getBindingResult().getGlobalErrors();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        return buildValidationErrors(globalErrors,
                fieldErrors);
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(NoSuchElementException.class)
    public FieldErrorOutputDto handleValidationError(NoSuchElementException exception) {

        return new FieldErrorOutputDto(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFileFormatException.class)
    public FieldErrorOutputDto handleValidationError(InvalidFileFormatException exception) {

        return new FieldErrorOutputDto(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidRangeDateException.class)
    public FieldErrorOutputDto handleValidationError(InvalidRangeDateException exception) {

        return new FieldErrorOutputDto(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(IllegalArgumentException.class)
    public FieldErrorOutputDto handleValidationError(IllegalArgumentException exception) {

        return new FieldErrorOutputDto(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler( ResponseStatusException.class)
    public FieldErrorOutputDto handleValidationError( ResponseStatusException exception) {

        return new FieldErrorOutputDto(exception.getMessage());
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatDateException.class)
    public ResponseEntity<String> handleInvalidDateFormatException(InvalidFormatDateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatNumberException.class)
    public ResponseEntity<String> handleInvalidFormatNumberException(InvalidFormatNumberException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ListIsEmptyException.class)
    public ResponseEntity<String> handleListIsEmptyException(ListIsEmptyException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(CustomerNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    private ValidationErrorsOutputDto buildValidationErrors(List<ObjectError> globalErrors,
                                                            List<FieldError> fieldErrors) {
        ValidationErrorsOutputDto validationErrors = new ValidationErrorsOutputDto();

        globalErrors.forEach(error -> validationErrors.addError(getErrorMessage(error)));

        fieldErrors.forEach(error -> {
            String errorMessage = getErrorMessage(error);
            validationErrors.addFieldError(error.getField(), errorMessage);
        });
        return validationErrors;
    }

    private String getErrorMessage(ObjectError error) {
        return messageSource.getMessage(error, LocaleContextHolder.getLocale());
    }


}
