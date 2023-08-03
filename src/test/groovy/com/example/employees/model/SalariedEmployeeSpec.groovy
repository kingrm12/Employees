package com.example.employees.model

import com.example.employees.constants.WorkConstants
import com.example.employees.exceptions.InsufficientVacationDaysException
import jakarta.validation.ValidationException
import spock.lang.Specification
import spock.lang.Unroll

class SalariedEmployeeSpec extends Specification {

    @Unroll('work - correctly increments #vacationDays vacation days when the employee works #daysWorked')
    def 'work - correctly increments vacation days when the employee works'() {
        given: 'I have a salaried employee created'
        SalariedEmployee salariedEmployee = new SalariedEmployee()

        when: 'I work a number of days'
        salariedEmployee.work(daysWorked)

        then: 'I should get the expected number of vacation days'
        salariedEmployee.vacationDays == vacationDays

        where:
        daysWorked | vacationDays
        0          | 0f
        10         | 0.5769231f
        23         | 1.3269231f
        17         | 0.9807692f
        52         | 3f
        85         | 4.9038463f
        130        | 7.5f
        150        | 8.653846f
        230        | 13.269231f
        260        | 15f
    }

    def 'work - does not allow an employee to work more than there are work days in a work year'() {
        given: 'I have a salaried employee created'
        SalariedEmployee salariedEmployee = new SalariedEmployee()

        when: 'I try to work more work days than allowed in a year'
        salariedEmployee.work(WorkConstants.WORK_DAYS_IN_A_WORK_YEAR + 1)

        then: 'I should get an exception stating I cannot work that may days at once'
        ValidationException ve = thrown()
        ve.message == Employee.MAX_WORK_DAYS_LIMIT
    }

    @Unroll('takeVacation - decrements available #accruedVacationDays accrued vacation days when #vacationDays vacation days are taken')
    def 'takeVacation - decrements available accrued vacation days when vacation days are taken'() {
        given: 'I have a salaried employee created'
        SalariedEmployee salariedEmployee = new SalariedEmployee()

        and: 'I have some vacation days accrued'
        salariedEmployee.vacationDays = accruedVacationDays

        when: 'I take vacation days'
        salariedEmployee.takeVacation(vacationDays)

        then: 'I should have the correct number of vacation days remaining in my bank'
        salariedEmployee.vacationDays == (float) (accruedVacationDays - vacationDays)

        where:
        accruedVacationDays | vacationDays
        10f                 | 3f
        10f                 | 2.4f
        20f                 | 3.5f
        30f                 | 5.4f
    }

    def 'takeVacation - prevents employees from taking more vacation than available'() {
        given: 'I have an hourly employee'
        SalariedEmployee salariedEmployee = new SalariedEmployee()

        and: 'I have some vacation days accrued'
        float vacationDays = new Random().nextFloat(15, 30)
        salariedEmployee.vacationDays = vacationDays

        when: 'I try to take more vacation days than I have accrued'
        salariedEmployee.takeVacation((float) (vacationDays + 1f))

        then: 'I should be told I have insufficient vacation days'
        thrown InsufficientVacationDaysException
    }
}