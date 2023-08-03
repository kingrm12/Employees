package com.example.employees.service

import com.example.employees.exceptions.NotFoundException
import com.example.employees.model.TimeCard
import org.apache.commons.lang3.RandomStringUtils
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class TimeCardServiceSpec extends Specification {

    @Autowired
    TimeCardService timeCardService

    @SpringBean
    EmployeeService employeeService = Mock(EmployeeService)

    def 'create - records the work for the employee'() {
        given: 'a time card needs to be created'
        UUID employeeId = UUID.randomUUID()
        int daysWorked = new Random().nextInt(10, 30)
        TimeCard timeCard = new TimeCard(employeeId: employeeId, daysWorked: daysWorked)

        when: 'I create the time card'
        timeCardService.create(timeCard)

        then: 'the work should be recorded for the employee'
        1 * employeeService.recordWork(timeCard) >> {}
    }

    def 'create - throws an exception when the employee cannot be found'() {
        given: 'a time card needs to be created'
        UUID employeeId = UUID.randomUUID()
        int daysWorked = new Random().nextInt(10, 30)
        TimeCard timeCard = new TimeCard(employeeId: employeeId, daysWorked: daysWorked)

        and: 'the employee does not exist'
        String message = RandomStringUtils.randomAlphanumeric(20)
        employeeService.recordWork(timeCard) >> { throw new NotFoundException(message) }

        when: 'I try to create the time card'
        timeCardService.create(timeCard)

        then: 'the employee should not be found'
        NotFoundException nfe = thrown()
        nfe.message == message
    }
}
