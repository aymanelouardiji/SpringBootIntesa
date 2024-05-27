package com.demo.controller;


import com.demo.model.Employee;
import com.demo.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private static final Logger logger =  LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        logger.info("Request received to fetch all employees");
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long employeeId) {
        logger.info("Request received to fetch employee with ID: {}", employeeId);
        Employee employee = employeeService.getEmployeeById(employeeId);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Employee> createFullTimeEmployee(@RequestBody Employee employee) {
        logger.info("Request received to create a new employee: {}", employee);
        Employee employee1 = employeeService.saveNewEmployee(employee);
        return new ResponseEntity<>(employee1,HttpStatus.CREATED);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long employeeId, @RequestBody Employee employee) {
        logger.info("Request received to update employee with ID: {}", employeeId);
        try {
            Employee updatedEmployee = employeeService.updateEmployee(employeeId, employee);
            logger.info("Employee with ID: {} successfully updated", employeeId);
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            logger.error("Error updating employee with ID: {}. {}", employeeId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeId) {
        logger.info("Request received to delete employee with ID: {}", employeeId);
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/departments")
    public ResponseEntity<Map<String, List<Employee>>> getEmployeesByDepartment() {
        logger.info("Request received to fetch employees by department");
        Map<String, List<Employee>> employeesByDepartment = employeeService.getEmployeesByDepartment();
        return new ResponseEntity<>(employeesByDepartment, HttpStatus.OK);
    }

    @GetMapping("/tasks")
    public ResponseEntity<Map<Long, List<Employee>>> getEmployeesByTask() {
        logger.info("Request received to fetch employees by task");
        Map<Long, List<Employee>> employeesByTask = employeeService.getEmployeesByTask();
        return new ResponseEntity<>(employeesByTask, HttpStatus.OK);
    }
}
