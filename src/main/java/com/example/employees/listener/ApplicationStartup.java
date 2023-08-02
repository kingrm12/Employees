package com.example.employees.listener;

import com.example.employees.model.HourlyEmployee;
import com.example.employees.model.Manager;
import com.example.employees.model.SalariedEmployee;
import com.example.employees.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final EmployeeService employeeService;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        for (int i = 0; i < 10; i++) {
            employeeService.create(new HourlyEmployee());
            employeeService.create(new SalariedEmployee());
            employeeService.create(new Manager());
        }
    }
}