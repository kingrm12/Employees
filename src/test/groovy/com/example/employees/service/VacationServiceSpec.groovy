package com.example.employees.service

import com.example.employees.exceptions.NotFoundException
import com.example.employees.model.VacationRequest
import org.apache.commons.lang3.RandomStringUtils
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class VacationServiceSpec extends Specification {

    @Autowired
    VacationService vacationService

    @SpringBean
    EmployeeService employeeService = Mock(EmployeeService)

    def 'create - requests vacation for an employee'() {
        given: 'a vacation request needs to be created'
        UUID employeeId = UUID.randomUUID()
        float vacationDaysRequested = new Random().nextFloat(3, 10)
        VacationRequest vacationRequest = new VacationRequest(employeeId: employeeId, vacationDaysRequested: vacationDaysRequested)

        when: 'I create the vacation request'
        vacationService.create(vacationRequest)

        then: 'vacation should be requested for the employee'
        employeeService.requestVacation(vacationRequest) >> {}
    }

    def 'create - throws an exception when the employee cannot be found'() {
        given: 'a vacation request needs to be created'
        UUID employeeId = UUID.randomUUID()
        float vacationDaysRequested = new Random().nextFloat(3, 10)
        VacationRequest vacationRequest = new VacationRequest(employeeId: employeeId, vacationDaysRequested: vacationDaysRequested)

        and: 'the employee does not exist'
        String message = RandomStringUtils.randomAlphanumeric(20)
        employeeService.requestVacation(vacationRequest) >> { throw new NotFoundException(message) }

        when: 'I try to create the time card'
        vacationService.create(vacationRequest)

        then: 'the employee should not be found'
        NotFoundException nfe = thrown()
        nfe.message == message
    }
}
