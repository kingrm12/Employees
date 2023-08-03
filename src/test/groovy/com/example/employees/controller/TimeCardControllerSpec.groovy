package com.example.employees.controller

import com.example.employees.exceptions.NotFoundException
import com.example.employees.model.SalariedEmployee
import com.example.employees.model.TimeCard
import com.example.employees.service.TimeCardService
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.lang3.RandomStringUtils
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@AutoConfigureMockMvc
@WebMvcTest(controllers = [TimeCardController])
class TimeCardControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @SpringBean
    TimeCardService timeCardService = Mock()

    @Autowired
    ObjectMapper objectMapper

    def 'create - should create a new time card'() {
        given: 'I have a new time card I want to create for an employee'
        SalariedEmployee salariedEmployee = new SalariedEmployee()
        int daysWorked = new Random().nextInt(5, 10)
        TimeCard timeCard = new TimeCard(employeeId: salariedEmployee.id, daysWorked: daysWorked)

        when: 'I create a new time card'
        MockHttpServletResponse response = mockMvc.perform(
                post(TimeCardController.TIME_CARDS_PATH + "/")
                        .content(objectMapper.writeValueAsString(timeCard))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().response

        then: 'the time card should be created successfully'
        1 * timeCardService.create(timeCard) >> { }

        and: 'the result should be NO_CONTENT'
        response.status == HttpStatus.NO_CONTENT.value()
    }

    def 'create - should return an error if the employee cannot be found'() {
        given: 'I have a new time card I want to create for a non-existent employee'
        SalariedEmployee salariedEmployee = new SalariedEmployee()
        int daysWorked = new Random().nextInt(5, 10)
        TimeCard timeCard = new TimeCard(employeeId: salariedEmployee.id, daysWorked: daysWorked)

        and: 'an error message will be returned'
        String message = RandomStringUtils.randomAlphanumeric(20)

        when: 'I try to create a new time card'
        MockHttpServletResponse response = mockMvc.perform(
                post(TimeCardController.TIME_CARDS_PATH + "/")
                        .content(objectMapper.writeValueAsString(timeCard))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().response

        then: 'the time card should return an error'
        1 * timeCardService.create(timeCard) >> { throw new NotFoundException(message) }

        and: 'the result should be NOT_FOUND'
        response.status == HttpStatus.NOT_FOUND.value()

        and: 'I should get the expected error message'
        response.contentAsString == message
    }
}
