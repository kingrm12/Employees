package com.example.employees.service;

import com.example.employees.exceptions.InsufficientVacationDaysException;
import com.example.employees.exceptions.NotFoundException;
import com.example.employees.model.TimeCard;
import com.example.employees.model.VacationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VacationService {

    private final EmployeeService employeeService;

    /**
     * Create a vacation request. The request could be persisted, but for now only deducts the requested hours from the
     * employee's bank.
     *
     * @param vacationRequest the vacation days requested for an employee ID
     *
     * @throws InsufficientVacationDaysException if the employee has insufficient vacation days banked
     * @throws NotFoundException                 if the employee cannot be found by ID
     */
    public void create(VacationRequest vacationRequest) throws InsufficientVacationDaysException, NotFoundException {
        employeeService.requestVacation(vacationRequest);
    }
}
