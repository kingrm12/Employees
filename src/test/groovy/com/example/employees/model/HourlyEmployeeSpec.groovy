package com.example.employees.model

import com.example.employees.constants.WorkConstants
import com.example.employees.exceptions.InsufficientVacationDaysException
import jakarta.validation.ValidationException
import spock.lang.Specification
import spock.lang.Unroll

class HourlyEmployeeSpec extends Specification {

    @Unroll('work - correctly increments #vacationDays vacation days when the employee works #daysWorked')
    def 'work - correctly increments vacation days when the employee works'() {
        given: 'I have an hourly employee created'
        HourlyEmployee hourlyEmployee = new HourlyEmployee()

        when: 'I work a number of days'
        hourlyEmployee.work(daysWorked)

        then: 'I should get the expected number of vacation days'
        hourlyEmployee.vacationDays == vacationDays

        where:
        daysWorked | vacationDays
        0          | 0f
        10         | 0.3846154f
        23         | 0.88461536f
        26         | 1f
        52         | 2f
        56         | 2.1538463f
        130        | 5f
        150        | 5.769231f
        230        | 8.846154f
        260        | 10f
    }

    def 'work - does not allow an employee to work more than there are work days in a work year'() {
        given: 'I have an hourly employee created'
        HourlyEmployee hourlyEmployee = new HourlyEmployee()

        when: 'I try to work more work days than allowed in a year'
        hourlyEmployee.work(WorkConstants.WORK_DAYS_IN_A_WORK_YEAR + 1)

        then: 'I should get an exception stating I cannot work that may days at once'
        ValidationException ve = thrown()
        ve.message == Employee.MAX_WORK_DAYS_LIMIT
    }

    @Unroll('takeVacation - decrements available #accruedVacationDays accrued vacation days when #vacationDays vacation days are taken')
    def 'takeVacation - decrements available accrued vacation days when vacation days are taken'() {
        given: 'I have an hourly employee created'
        HourlyEmployee hourlyEmployee = new HourlyEmployee()

        and: 'I have some vacation days accrued'
        // private member is directly modifiable through the magic of groovy reflection, so clean and easy!
        hourlyEmployee.vacationDays = accruedVacationDays

        when: 'I take vacation days'
        hourlyEmployee.takeVacation(vacationDays)

        then: 'I should have the correct number of vacation days remaining in my bank'
        hourlyEmployee.vacationDays == (float) (accruedVacationDays - vacationDays)

        where:
        accruedVacationDays | vacationDays
        10f                 | 3f
        10f                 | 2.4f
        20f                 | 3.5f
        30f                 | 5.4f
    }

    def 'takeVacation - prevents employees from taking more vacation than available'() {
        given: 'I have an hourly employee'
        HourlyEmployee hourlyEmployee = new HourlyEmployee()

        and: 'I have some vacation days accrued'
        float vacationDays = new Random().nextFloat(15, 30)
        hourlyEmployee.vacationDays = vacationDays

        when: 'I try to take more vacaation days than I have accrued'
        hourlyEmployee.takeVacation((float) (vacationDays + 1f))

        then: 'I should be told I have insufficient vacation days'
        thrown InsufficientVacationDaysException
    }
}