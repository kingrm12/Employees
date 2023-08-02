package com.example.employees.controller;

import com.example.employees.exceptions.InsufficientVacationDaysException;
import com.example.employees.exceptions.NotFoundException;
import com.example.employees.model.VacationRequest;
import com.example.employees.service.VacationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("vacationrequests")
@RequiredArgsConstructor
@RestController
public class VacationController {

    private final VacationService vacationService;

    /**
     * Create a new vacation request
     *
     * @param vacationRequest the vacation request to create
     */
    @PostMapping(path = "/", produces = "application/json")
    public void create(@RequestBody VacationRequest vacationRequest) throws InsufficientVacationDaysException,
            NotFoundException {
        vacationService.create(vacationRequest);
    }
}
