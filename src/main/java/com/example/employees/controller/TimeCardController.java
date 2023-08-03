package com.example.employees.controller;

import com.example.employees.exceptions.NotFoundException;
import com.example.employees.model.TimeCard;
import com.example.employees.service.TimeCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping(TimeCardController.TIME_CARDS_PATH)
@RequiredArgsConstructor
@RestController
public class TimeCardController {

    public static final String TIME_CARDS_PATH = "/timecards";

    private final TimeCardService timeCardService;

    /**
     * Create a new time card
     *
     * @param timeCard the time card to create
     */
    @PostMapping(path = "/")
    public ResponseEntity<Void> create(@RequestBody TimeCard timeCard) throws NotFoundException {
        timeCardService.create(timeCard);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
