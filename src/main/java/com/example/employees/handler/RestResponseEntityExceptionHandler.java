package com.example.employees.handler;

import com.example.employees.exceptions.InsufficientVacationDaysException;
import com.example.employees.exceptions.NotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { NotFoundException.class })
    protected ResponseEntity<String> handleNotFoundException(NotFoundException nfe) {
        return new ResponseEntity<>(nfe.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { InsufficientVacationDaysException.class })
    protected ResponseEntity<String> handleInsufficientVacationDaysException(InsufficientVacationDaysException ivde) {
        return new ResponseEntity<>(ivde.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { ValidationException.class })
    protected ResponseEntity<String> handleValidationException(ValidationException ve) {
        return new ResponseEntity<>(ve.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}