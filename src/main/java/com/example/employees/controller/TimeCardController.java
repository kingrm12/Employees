package com.example.employees.controller;

import com.example.employees.exceptions.NotFoundException;
import com.example.employees.model.TimeCard;
import com.example.employees.service.TimeCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("timecards")
@RequiredArgsConstructor
@RestController
public class TimeCardController {

    private final TimeCardService timeCardService;

    /**
     * Create a new time card
     *
     * @param timeCard the time card to create
     */
    @PostMapping(path = "/", produces = "application/json")
    public void create(@RequestBody TimeCard timeCard) throws NotFoundException {
        timeCardService.create(timeCard);
    }
}
