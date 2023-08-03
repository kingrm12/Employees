package com.example.employees.model;

import com.example.employees.exceptions.InsufficientVacationDaysException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

import static com.example.employees.constants.WorkConstants.WORK_DAYS_IN_A_WORK_YEAR;

@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HourlyEmployee.class, name = "hourly"),
        @JsonSubTypes.Type(value = Manager.class, name = "manager"),
        @JsonSubTypes.Type(value = SalariedEmployee.class, name = "salaried") }
)
public abstract class Employee {

    public static final String MAX_WORK_DAYS_LIMIT = "An employee cannot work more than " + WORK_DAYS_IN_A_WORK_YEAR
            + " days at a time";

    private final UUID id = UUID.randomUUID();

    // This property is "not writable externally" because it's private. There is, however, a setter generated for it.
    // If this value is only written to through a public setter, it is only writable internally, even if the setter
    // allows public access to indirectly modify it.
    // If the constraint is intended to mean "we should only be able to update the value via work being done", we would
    // need to update the tests to do so and use this to remove the setter:
    // @Setter(AccessLevel.NONE)
    private float vacationDays;
    private int vacationDaysAccumulatedPerWorkYear;

    public void takeVacation(float vacationDaysUsed) throws InsufficientVacationDaysException {
        if (vacationDaysUsed > vacationDays) {
            throw new InsufficientVacationDaysException();
        }
        this.vacationDays -= vacationDaysUsed;
    }

    public void work(int daysWorked) {
        if (daysWorked > WORK_DAYS_IN_A_WORK_YEAR) {
            throw new ValidationException(MAX_WORK_DAYS_LIMIT);
        }
        float vacationDaysAccrued = (float) daysWorked * vacationDaysAccumulatedPerWorkYear / WORK_DAYS_IN_A_WORK_YEAR;
        this.vacationDays += vacationDaysAccrued;
    }
}
