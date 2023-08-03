package com.example.employees.controller;

import com.example.employees.exceptions.NotFoundException;
import com.example.employees.model.Employee;
import com.example.employees.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping(EmployeeController.EMPLOYEES_PATH)
@RestController
public class EmployeeController {

    public static final String EMPLOYEES_PATH = "/employees";

    private final EmployeeService employeeService;

    /**
     * Create a new Employee
     *
     * @param employee the employee to create
     *
     * @return the unique identifier of the employee created
     */
    @PostMapping(path = "/")
    public ResponseEntity<UUID> create(@RequestBody Employee employee) {

        return new ResponseEntity<>(employeeService.create(employee), HttpStatus.CREATED);
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
    @GetMapping(path = "/{id}")
    public ResponseEntity<Employee> read(@PathVariable UUID id) throws NotFoundException {
        return new ResponseEntity<>( employeeService.read(id), HttpStatus.OK);
    }

    /**
     * Delete an Employee by their unique ID
     *
     * @param id the employee's unique identifier
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        employeeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * List all current employees. Particularly if there were persistent storage backing the implementation, this would
     * be a great place to add pagination or filtering.
     *
     * @return a list of employees
     */
    @GetMapping(path = "/")
    public ResponseEntity<List<Employee>> list() {
        return new ResponseEntity<>(employeeService.list(), HttpStatus.OK);
    }
}
