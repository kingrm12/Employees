package com.example.employees.model;

import static com.example.employees.constants.WorkConstants.INITIAL_VACATION_DAYS;

public class HourlyEmployee extends Employee {

    public static final int VACATION_DAYS_ACCUMULATED_PER_WORK_YEAR = 10;

    public HourlyEmployee() {
        super(INITIAL_VACATION_DAYS, VACATION_DAYS_ACCUMULATED_PER_WORK_YEAR);
    }
}
