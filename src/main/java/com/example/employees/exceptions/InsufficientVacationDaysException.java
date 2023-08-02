package com.example.employees.exceptions;

public class InsufficientVacationDaysException extends Exception {

    private static final String INSUFFICIENT_VACATION_DAYS_FOR_REQUEST = "Insufficient vacation days accrued to fulfill vacation request";

    public InsufficientVacationDaysException() {
        super(INSUFFICIENT_VACATION_DAYS_FOR_REQUEST);
    }
}
