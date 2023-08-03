package com.example.employees.controller

import com.example.employees.exceptions.NotFoundException
import com.example.employees.model.SalariedEmployee
import com.example.employees.model.VacationRequest
import com.example.employees.service.VacationService
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
@WebMvcTest(controllers = [VacationController])
class VacationControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @SpringBean
    VacationService vacationService = Mock()

    @Autowired
    ObjectMapper objectMapper

    def 'create - should create a new vacation request'() {
        given: 'I have a new vacation request I want to create for an employee'
        SalariedEmployee salariedEmployee = new SalariedEmployee()
        float vacationDaysRequested = new Random().nextFloat(5, 10)
        VacationRequest vacationRequest = new VacationRequest(employeeId: salariedEmployee.id, vacationDaysRequested: vacationDaysRequested)

        when: 'I create a new vacation request'
        MockHttpServletResponse response = mockMvc.perform(
                post(VacationController.VACATIONS_PATH + "/")
                        .content(objectMapper.writeValueAsString(vacationRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().response

        then: 'the vacation request should be created successfully'
        1 * vacationService.create(vacationRequest) >> { }

        and: 'the result should be NO_CONTENT'
        response.status == HttpStatus.NO_CONTENT.value()
    }

    def 'create - should return an error if the employee cannot be found'() {
        given: 'I have a new vacation request I want to create for a non-existent employee'
        SalariedEmployee salariedEmployee = new SalariedEmployee()
        float vacationDaysRequested = new Random().nextFloat(5, 10)
        VacationRequest vacationRequest = new VacationRequest(employeeId: salariedEmployee.id, vacationDaysRequested: vacationDaysRequested)

        and: 'an error message will be returned'
        String message = RandomStringUtils.randomAlphanumeric(20)

        when: 'I try to create a new vacation request'
        MockHttpServletResponse response = mockMvc.perform(
                post(VacationController.VACATIONS_PATH + "/")
                        .content(objectMapper.writeValueAsString(vacationRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().response

        then: 'the vacation request should return an error'
        1 * vacationService.create(vacationRequest) >> { throw new NotFoundException(message) }

        and: 'the result should be NOT_FOUND'
        response.status == HttpStatus.NOT_FOUND.value()
        
        and: 'I should get the expected error message'
        response.contentAsString == message
    }
}
