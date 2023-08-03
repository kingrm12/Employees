package com.example.employees.service;

import com.example.employees.exceptions.NotFoundException;
import com.example.employees.model.TimeCard;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Service
@Validated
public class TimeCardService {

    private final EmployeeService employeeService;

    /**
     * Create a new time card. The time card could be persisted, but for now only updates the employee's record of hours
     * worked.
     *
     * @param timeCard the time card to record
     *
     * @throws NotFoundException if the employee cannot be found by ID
     */
    public void create(@Valid TimeCard timeCard) throws NotFoundException {
        employeeService.recordWork(timeCard);
    }
}
