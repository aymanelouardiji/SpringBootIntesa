package com.demo.service;

import com.demo.model.Employee;
import com.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        employee = new Employee(1L, "Ayman", "yy", "yyy", "zz.zz@example.com");
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        when(employeeRepository.findAll()).thenReturn(employees);
        assertEquals(1, employeeService.getAllEmployees().size());
    }

    @Test
    void testGetEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        assertEquals("Ayman", employeeService.getEmployeeById(1L).getFirstName());
    }

    @Test
    void testSaveNewEmployee() {
        when(employeeRepository.save(employee)).thenReturn(employee);
        Employee savedEmployee = employeeService.saveNewEmployee(employee);
        assertEquals(employee, savedEmployee);
    }

    @Test
    void testUpdateEmployee() {
        Employee updatedEmployee = new Employee(1L,"ayman", "eee", "grr", "dff.eee@example.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(updatedEmployee);

        Employee result = employeeService.updateEmployee(1L, updatedEmployee);

        assertEquals(updatedEmployee.getFirstName(), result.getFirstName());
        assertEquals(updatedEmployee.getLastName(), result.getLastName());
        assertEquals(updatedEmployee.getDepartment(), result.getDepartment());
        assertEquals(updatedEmployee.getEmail(), result.getEmail());
    }

    @Test
    void testUpdateEmployeeNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> employeeService.updateEmployee(1L, employee));
        assertEquals("Employee not found for this id : 1", exception.getReason());
    }

    @Test
    void testDeleteEmployee() {
        employeeService.deleteEmployee(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetEmployeesByDepartment() {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        when(employeeRepository.findAll()).thenReturn(employees);
        assertEquals(1, employeeService.getEmployeesByDepartment().size());
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> employeeService.getEmployeeById(1L));
        assertEquals("Employee not found for this id : 1", exception.getReason());
    }
}
