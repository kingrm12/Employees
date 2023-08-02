package com.example.employees.model;

import lombok.Data;

import java.util.UUID;

@Data
public class TimeCard {

    UUID employeeId;
    int daysWorked;

}
