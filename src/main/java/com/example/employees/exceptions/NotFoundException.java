package com.example.employees.exceptions;

public class NotFoundException extends Exception {

    public static final String EMPLOYEE_NOT_FOUND = "Employee [%s] not found";

    public NotFoundException(String message) {
        super(message);
    }
}
