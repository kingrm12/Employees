package com.example.employees.model;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
public class VacationRequest {

    UUID employeeId;
    @Positive
    float vacationDaysRequested;
}
