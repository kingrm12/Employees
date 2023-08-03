package com.example.employees.model

import com.example.employees.constants.WorkConstants
import com.example.employees.exceptions.InsufficientVacationDaysException
import jakarta.validation.ValidationException
import spock.lang.Specification
import spock.lang.Unroll

class ManagerSpec extends Specification {

    @Unroll('work - correctly increments #vacationDays vacation days when the employee works #daysWorked')
    def 'work - correctly increments vacation days when the employee works'() {
        given: 'I have a manager created'
        Manager manager = new Manager()

        when: 'I work a number of days'
        manager.work(daysWorked)

        then: 'I should get the expected number of vacation days'
        manager.vacationDays == vacationDays

        where:
        daysWorked | vacationDays
        0          | 0f
        10         | 1.1538461f
        23         | 2.6538463f
        26         | 3f
        52         | 6f
        56         | 6.4615383f
        130        | 15f
        150        | 17.307692f
        230        | 26.538462f
        260        | 30f
    }

    def 'work - does not allow an employee to work more than there are work days in a work year'() {
        given: 'I have a manager created'
        Manager manager = new Manager()

        when: 'I try to work more work days than allowed in a year'
        manager.work(WorkConstants.WORK_DAYS_IN_A_WORK_YEAR + 1)

        then: 'I should get an exception stating I cannot work that may days at once'
        ValidationException ve = thrown()
        ve.message == Employee.MAX_WORK_DAYS_LIMIT
    }

    @Unroll('takeVacation - decrements available #accruedVacationDays accrued vacation days when #vacationDays vacation days are taken')
    def 'takeVacation - decrements available accrued vacation days when vacation days are taken'() {
        given: 'I have a manager created'
        Manager manager = new Manager()

        and: 'I have some vacation days accrued'
        manager.vacationDays = accruedVacationDays

        when: 'I take vacation days'
        manager.takeVacation(vacationDays)

        then: 'I should have the correct number of vacation days remaining in my bank'
        manager.vacationDays == (float) (accruedVacationDays - vacationDays)

        where:
        accruedVacationDays | vacationDays
        10f                 | 3f
        10f                 | 2.4f
        20f                 | 3.5f
        30f                 | 5.4f
    }

    def 'takeVacation - prevents employees from taking more vacation than available'() {
        given: 'I have a manaager'
        Manager manager = new Manager()

        and: 'I have some vacation days accrued'
        float vacationDays = new Random().nextFloat(15, 30)
        manager.vacationDays = vacationDays

        when: 'I try to take more vacation days than I have accrued'
        manager.takeVacation((float) (vacationDays + 1f))

        then: 'I should be told I have insufficient vacation days'
        thrown InsufficientVacationDaysException
    }
}