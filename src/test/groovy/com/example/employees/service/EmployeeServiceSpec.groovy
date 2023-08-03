package com.example.employees.service

import com.example.employees.exceptions.NotFoundException
import com.example.employees.model.Employee
import com.example.employees.model.HourlyEmployee
import com.example.employees.model.Manager
import com.example.employees.model.SalariedEmployee
import com.example.employees.model.TimeCard
import com.example.employees.model.VacationRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class EmployeeServiceSpec extends Specification {

    @Autowired
    EmployeeService employeeService

    def cleanup() {
        /*
          Clean up after each test. We might do this on setup, but that just means someone else didn't clean up after
          themselves.
         */
        employeeService.clear()
    }

    def 'create, read, and delete - creates a new employee'() {
        given: 'I have an employee I want to create'
        Manager manager = new Manager()

        when: 'I create the employee'
        employeeService.create(manager)

        and: 'I read back the employee'
        Employee readEmployee = employeeService.read(manager.id)

        then: 'I should get back the same employee'
        readEmployee == manager

        when: 'I delete the employee'
        employeeService.delete(manager.id)

        and: 'attempt to read the employee back'
        employeeService.read(manager.id)

        then: 'the employee should not be found'
        NotFoundException nfe = thrown()
        nfe.message == String.format(NotFoundException.EMPLOYEE_NOT_FOUND, manager.id)
    }

    /*
       This would be a place to talk to product and inquire about desired user expectations. It's not unreasonable for
       a user to expect that the employee exists before issuing a delete request. In that case, the user may want to be
       informed that they asked for a non-existent employee to be deleted and we could kick back an exception.
     */
    def 'delete - does not throw an exception if the employee does not exist'() {
        given: 'I have an employee ID that does not exist'
        UUID employeeId = UUID.randomUUID()

        when: 'I try to delete the employee'
        employeeService.delete(employeeId)

        then: 'I should not get an exception because they employee is confirmed to not exist'
        noExceptionThrown()
    }

    def 'list - should return all employees'() {
        given: 'I have created a number of employees'
        int employeeCount = 30
        List<Class> employeeTypes = [HourlyEmployee, SalariedEmployee, Manager]
        List<Employee> employees = []
        employeeCount.times {
            Employee employee = (Employee) employeeTypes[new Random().nextInt(1, 3)].getDeclaredConstructor().newInstance()
            employeeService.create(employee)
            employees << employee
        }

        when: 'I list the current employees'
        List<Employee> employeeList = employeeService.list()

        then: 'I should get back the full list of employees'
        employees == employeeList
    }

    def 'recordWork - should perform work for an employee'() {
        given: 'I have a mock employee created'
        Employee employee = Mock()
        employee.getId() >> UUID.randomUUID()
        employeeService.create(employee)

        and: 'the employee has worked some amount of days'
        int daysWorked = new Random().nextInt(10, 30)

        when: 'I record work for the employee'
        employeeService.recordWork(new TimeCard(employeeId: employee.getId(), daysWorked: daysWorked))

        then: 'the work for the employee should be updated'
        1 * employee.work(daysWorked)
    }

    def 'recordWork - should throw an exception when the employee does not exist'() {
        given: 'I have a non-existent employee ID'
        UUID employeeId = UUID.randomUUID()

        and: 'we think the employee has worked some amount of days'
        int daysWorked = new Random().nextInt(10, 30)

        when: 'I try to record work for the employee'
        employeeService.recordWork(new TimeCard(employeeId: employeeId, daysWorked: daysWorked))

        then: 'I should be informed that the employee does not exist'
        NotFoundException nfe = thrown()
        nfe.message == String.format(NotFoundException.EMPLOYEE_NOT_FOUND, employeeId)
    }

    def 'requestVacation - should take vacation for an employee'() {
        given: 'I have a mock employee created'
        Employee employee = Mock()
        employee.getId() >> UUID.randomUUID()
        employeeService.create(employee)

        and: 'the employee wishes to request vacation time'
        float vacationDaysRequested = new Random().nextFloat(10, 30)

        when: 'I request vacation for the employee'
        employeeService.requestVacation(new VacationRequest(employeeId: employee.getId(), vacationDaysRequested: vacationDaysRequested))

        then: 'the vacation days should be deducted for the employee'
        1 * employee.takeVacation(vacationDaysRequested)
    }

    def 'requestVacation - should throw an exception when the employee does not exist'() {
        given: 'I have a non-existent employee ID'
        UUID employeeId = UUID.randomUUID()

        and: 'we think the employee want to take some vacation days'
        float vacationDaysRequested = new Random().nextFloat(10, 30)

        when: 'I try to request vacation time for the employee'
        employeeService.requestVacation(new VacationRequest(employeeId: employeeId, vacationDaysRequested: vacationDaysRequested))

        then: 'I should be informed that the employee does not exist'
        NotFoundException nfe = thrown()
        nfe.message == String.format(NotFoundException.EMPLOYEE_NOT_FOUND, employeeId)
    }
}