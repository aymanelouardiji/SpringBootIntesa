package com.demo.service;

import com.demo.model.Employee;
import com.demo.repository.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    private Employee employee;
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Before
    public void beforeEachTest() {
        employeeService = new EmployeeService(employeeRepository);
        this.employee = new Employee(1, "ayman", "el ouardiji","info","ayman@gmail.com");
    }

    @Test
    public void testFindEmployeeById(){
        when(employeeRepository.findById(1L));

        Employee foundEmployee = employeeService.getEmployeeById(1L);

        assertEquals(employee, foundEmployee);
    }

    @Test
    public void saveEmployee() {
        employeeService.saveNewEmployee(employee);
    }

    @Test
    public void deleteEmployee() {
        employeeService.deleteEmployee(1L);
    }
}