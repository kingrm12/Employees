package com.example.employees.service;

import com.example.employees.exceptions.InsufficientVacationDaysException;
import com.example.employees.exceptions.NotFoundException;
import com.example.employees.model.Employee;
import com.example.employees.model.TimeCard;
import com.example.employees.model.VacationRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.employees.exceptions.NotFoundException.EMPLOYEE_NOT_FOUND;

/**
 * If we had multiple storage implementations, this could be renamed to EphemeralEmployeeService or
 * ArrayListEmployeeService. EmployeeService would then become an interface, and we could do conditional DI in the
 * controller based on needs (e.g. @Primary, name-based DI)
 */
@Service
public class EmployeeService {

    List<Employee> employeeList = new ArrayList<>();

    /**
     * Create an employee within the ephemeral ArrayList storage.
     *
     * @param employee the employee to create
     *
     * @return the ID of the employee created
     */
    public UUID create(Employee employee) {
        employeeList.add(employee);
        return employee.getId();
    }

    /**
     * Read an employee from the ephemeral ArrayList storage.
     *
     * @param id the unique identifier of the employee
     *
     * @return the employee
     *
     * @throws NotFoundException if the employee cannot be found
     */
    public Employee read(UUID id) throws NotFoundException {
        Optional<Employee> optionalEmployee = employeeList.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst();
        return optionalEmployee.orElseThrow(() -> new NotFoundException(String.format(EMPLOYEE_NOT_FOUND, id)));
    }

    /**
     * Delete an employee from the ephemeral ArrayList storage. Does not throw an exception if the employee cannot be
     * found. Since we want the employee to no longer exist, mission accomplished.
     *
     * @param id the unique identifier of the employee
     */
    public void delete(UUID id) {
        employeeList.removeIf(e -> e.getId() == id);
    }

    /**
     * Clear the list of employees.
     */
    void clear() {
        employeeList = new ArrayList<>();
    }

    /**
     * List all current employees. This would be a great place for pagination and filtering. With large numbers of
     * employees, we'd run into memory constraints on this storage and response times would be unbounded.
     *
     * @return the list of current employees
     */
    public List<Employee> list() {
        return employeeList;
    }

    /**
     * Locate an employee and record their time worked.
     *
     * @param timeCard the time card for the given employee
     *
     * @throws NotFoundException if the employee cannot be found
     */
    void recordWork(TimeCard timeCard) throws NotFoundException {
        Optional<Employee> optionalEmployee = employeeList.stream()
                .filter(e -> e.getId().equals(timeCard.getEmployeeId()))
                .findFirst();

        if (optionalEmployee.isEmpty()) {
            throw new NotFoundException(String.format(EMPLOYEE_NOT_FOUND, timeCard.getEmployeeId()));
        }

        optionalEmployee.get().work(timeCard.getDaysWorked());
    }

    /**
     * Locate an employee and record vacation days used.
     *
     * @param vacationRequest the vacation request for the given employee
     *
     * @throws InsufficientVacationDaysException if the employee does not have sufficient vacation days for the request
     * @throws NotFoundException                 if the employee cannot be found
     */
    void requestVacation(VacationRequest vacationRequest) throws InsufficientVacationDaysException, NotFoundException {
        Optional<Employee> optionalEmployee = employeeList.stream()
                .filter(e -> e.getId().equals(vacationRequest.getEmployeeId()))
                .findFirst();

        if (optionalEmployee.isEmpty()) {
            throw new NotFoundException(String.format(EMPLOYEE_NOT_FOUND, vacationRequest.getEmployeeId()));
        }

        optionalEmployee.get().takeVacation(vacationRequest.getVacationDaysRequested());
    }
}
