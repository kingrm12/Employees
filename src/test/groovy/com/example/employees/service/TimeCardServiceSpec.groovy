package com.example.employees.service

import com.example.employees.constants.WorkConstants
import com.example.employees.exceptions.NotFoundException
import com.example.employees.model.TimeCard
import jakarta.validation.ValidationException
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

    def 'create - will not allow negative time to be worked'() {
        given: 'we have a time card with negative hours worked'
        UUID employeeId = UUID.randomUUID()
        int daysWorked = -1 * new Random().nextInt(10, 30)
        TimeCard timeCard = new TimeCard(employeeId: employeeId, daysWorked: daysWorked)

        when: 'I try to create the time card'
        timeCardService.create(timeCard)

        then: 'the time card should be recognized as invalid'
        ValidationException ve = thrown()
        ve.message == "create.timeCard.daysWorked: must be greater than 0"
    }

    def 'create - will not allow more hours than allowed in a work year'() {
        given: 'we have a time card with more hours worked than allowed in a work year'
        UUID employeeId = UUID.randomUUID()
        int daysWorked = WorkConstants.WORK_DAYS_IN_A_WORK_YEAR + 1
        TimeCard timeCard = new TimeCard(employeeId: employeeId, daysWorked: daysWorked)

        when: 'I try to create the time card'
        timeCardService.create(timeCard)

        then: 'the time card should be recognized as invalid'
        ValidationException ve = thrown()
        ve.message == "create.timeCard.daysWorked: must be less than or equal to " + WorkConstants.WORK_DAYS_IN_A_WORK_YEAR
    }
}
