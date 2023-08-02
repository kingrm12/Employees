package com.example.employees.service

import com.example.employees.model.Employee
import com.example.employees.model.Manager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class EmployeeServiceSpec extends Specification {

    @Autowired
    EmployeeService employeeService

    def 'create and read - creates a new employee'() {
        given: 'I have an employee I want to create'
        Manager manager = new Manager()

        when: 'I create the employee'
        employeeService.create(manager)

        and: 'I read back the employee'
        Employee readEmployee = employeeService.read(manager.id)

        then: 'I should get back the same employee'
        readEmployee == manager
    }
}
