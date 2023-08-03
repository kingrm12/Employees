package com.example.employees.handler

import com.example.employees.constants.WorkConstants
import com.example.employees.controller.TimeCardController
import com.example.employees.model.Manager
import com.example.employees.model.TimeCard
import com.example.employees.service.TimeCardService
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.validation.ValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc
@WebMvcTest(controllers = [TimeCardController])
class RestResponseEntityExceptionHandlerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @SpringBean
    TimeCardService timeCardService = Mock()

    @Autowired
    ObjectMapper objectMapper

    def 'a validation exception should be handled by our exception handler'() {
        given: 'I have an invalid time card to create'
        Manager manager = new Manager()
        TimeCard timeCard = new TimeCard(employeeId: manager.id, daysWorked: WorkConstants.WORK_DAYS_IN_A_WORK_YEAR + 1)

        and: 'an error message will be returned'
        String message = RandomStringUtils.randomAlphanumeric(20)

        when: 'I try to submit the invalid time card'
        MockHttpServletResponse response = mockMvc.perform(
                post(TimeCardController.TIME_CARDS_PATH + "/")
                        .content(objectMapper.writeValueAsString(timeCard))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().response

        then: 'the time card request should be invalid'
        1 * timeCardService.create(timeCard) >> { throw new ValidationException(message) }

        and: 'the response should be BAD_REQUEST'
        response.status == HttpStatus.BAD_REQUEST.value()

        and: 'we should get back the expected error message'
        response.contentAsString == message
    }
}