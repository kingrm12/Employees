package com.example.employees.controller;

import com.example.employees.exceptions.InsufficientVacationDaysException;
import com.example.employees.exceptions.NotFoundException;
import com.example.employees.model.VacationRequest;
import com.example.employees.service.VacationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(VacationController.VACATIONS_PATH)
@RequiredArgsConstructor
@RestController
public class VacationController {

    public static final String VACATIONS_PATH = "/vacationrequests";

    private final VacationService vacationService;

    /**
     * Create a new vacation request
     *
     * @param vacationRequest the vacation request to create
     */
    @PostMapping(path = "/")
    public ResponseEntity<Void> create(@RequestBody VacationRequest vacationRequest) throws InsufficientVacationDaysException,
            NotFoundException {
        vacationService.create(vacationRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
