package com.example.employees.controller;

import com.example.employees.exceptions.NotFoundException;
import com.example.employees.model.Employee;
import com.example.employees.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.employees.exceptions.NotFoundException.EMPLOYEE_NOT_FOUND;

@RequiredArgsConstructor
@RequestMapping("employees")
@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Create a new Employee
     *
     * @param employee the employee to create
     *
     * @return the unique identifier of the employee created
     */
    @PostMapping(path = "/", produces = "application/json")
    public UUID create(@RequestBody Employee employee) {
        return employeeService.create(employee);
    }

    /**
     * Read an Employee by their unique ID
     *
     * @param id the ID of the employee
     *
     * @return the employee
     *
     * @throws NotFoundException if the employee cannot be found
     */
    @GetMapping(path = "/{id}", produces = "application/json")
    public Employee read(@PathVariable UUID id) throws NotFoundException {
        return employeeService.read(id);
    }

    /**
     * Delete an Employee by their unique ID
     *
     * @param id the employee's unique identifier
     */
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable UUID id) {

    }

    /**
     * List all current employees. Particularly if there were persistent storage backing the implementation, this would
     * be a great place to add pagination or filtering.
     *
     * @return a list of employees
     */
    @GetMapping(path = "/")
    public List<Employee> list() {
        return employeeService.list();
    }
}
