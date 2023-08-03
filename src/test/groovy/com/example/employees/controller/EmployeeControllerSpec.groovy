package com.example.employees.controller


import com.example.employees.exceptions.NotFoundException
import com.example.employees.model.HourlyEmployee
import com.example.employees.model.Manager
import com.example.employees.service.EmployeeService
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@AutoConfigureMockMvc
@WebMvcTest(controllers = [EmployeeController])
class EmployeeControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @SpringBean
    EmployeeService employeeService = Mock()

    @Autowired
    ObjectMapper objectMapper

    def 'create - should create a new employee'() {
        given: 'I have an employee I want to create'
        HourlyEmployee hourlyEmployee = new HourlyEmployee()

        when: 'I create the employee'
        MockHttpServletResponse response = mockMvc.perform(
                post(EmployeeController.EMPLOYEES_PATH + "/")
                        .content(objectMapper.writeValueAsString(hourlyEmployee))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().response

        then: 'the employee should be created successfully'
        1 * employeeService.create(hourlyEmployee) >> hourlyEmployee.id

        and: 'the result should be CREATED'
        response.status == HttpStatus.CREATED.value()

        and: 'I should get back the employee ID'
        objectMapper.readValue(response.contentAsString, UUID) == hourlyEmployee.id
    }

    def 'read - should return an employee'() {
        given: 'I have an employee I want to read'
        HourlyEmployee hourlyEmployee = new HourlyEmployee()

        when: 'I read the employee'
        MockHttpServletResponse response = mockMvc.perform(
                get(EmployeeController.EMPLOYEES_PATH + '/' + hourlyEmployee.id))
                .andReturn().response

        then: 'the employee should be read successfully'
        1 * employeeService.read(hourlyEmployee.id) >> hourlyEmployee

        then: 'the result should be OK'
        response.status == HttpStatus.OK.value()

        and: 'I should get back the employee'
        objectMapper.readValue(response.contentAsString, HourlyEmployee) == hourlyEmployee
    }

    def 'read - should return an error message if the employee cannot be found'() {
        given: 'I have an non-existent employee ID'
        UUID employeId = UUID.randomUUID()

        and: 'an error message will be returned'
        String message = RandomStringUtils.randomAlphanumeric(20)

        when: 'I try read the employee'
        MockHttpServletResponse response = mockMvc.perform(
                get(EmployeeController.EMPLOYEES_PATH + '/' + employeId))
                .andReturn().response

        then: 'the employee should not be found'
        1 * employeeService.read(employeId) >> {  throw new NotFoundException(message) }

        and: 'the result should be NOT_FOUND'
        response.status == HttpStatus.NOT_FOUND.value()

        and: 'I should get back the error message'
        response.contentAsString == message
    }

    def 'delete - should delete an employee'() {
        given: 'I have an employee I want to delete'
        Manager manager = new Manager()

        when: 'I delete the employee'
        MockHttpServletResponse response = mockMvc.perform(
                delete(EmployeeController.EMPLOYEES_PATH + '/' + manager.id))
                .andReturn().response

        then: 'the employee should be successfully deleted'
        1 * employeeService.delete(manager.id) >> { }

        and: 'the result should be NO_CONTENT'
        response.status == HttpStatus.NO_CONTENT.value()
    }
}
