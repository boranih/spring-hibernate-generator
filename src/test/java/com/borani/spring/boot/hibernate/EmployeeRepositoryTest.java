package com.borani.spring.boot.hibernate;

import com.borani.spring.boot.hibernate.entity.Employee;
import com.borani.spring.boot.hibernate.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class EmployeeRepositoryTest {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeRepositoryTest(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Test
    public void testInsertEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setAge(30);

        Employee savedEmployee = employeeRepository.save(employee);

        // JUnit 5 Assertions
        assertNotNull(savedEmployee.getEmployeeId(), "Employee ID should not be null after save");
        assertEquals("John", savedEmployee.getFirstName(), "First name should be John");
        assertEquals("Doe", savedEmployee.getLastName(), "Last name should be Doe");
        assertEquals(30, savedEmployee.getAge(), "Age should be 30");

        System.out.println("Saved Employee: " + savedEmployee);
    }
}
