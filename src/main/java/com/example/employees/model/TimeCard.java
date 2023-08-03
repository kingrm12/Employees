package com.example.employees.model;

import com.example.employees.constants.WorkConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
public class TimeCard {

    UUID employeeId;
    @Positive @Max(WorkConstants.WORK_DAYS_IN_A_WORK_YEAR)
    int daysWorked;
}
