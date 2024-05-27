package com.demo.service;

import com.demo.model.Employee;
import com.demo.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private static final Logger logger =LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;

    /**
     * Constructor for EmployeeService.
     *
     * @param employeeRepository The repository for accessing employee data.
     */
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Retrieve all employees.
     *
     * @return A list of all employees.
     */
    public List<Employee> getAllEmployees(){
        logger.info("Fetching all employees");
        return employeeRepository.findAll();
    }

    /**
     * Retrieve an employee by ID.
     *
     * @param employeeId The ID of the employee to retrieve.
     * @return The employee with the specified ID.
     * @throws ResponseStatusException If no employee with the given ID is found.
     */
    public Employee getEmployeeById(Long employeeId){
        logger.info("Fetching employee with ID: {}", employeeId);
        return employeeRepository.findById(employeeId)
                .orElseThrow(() ->{logger.error("Employee not found for ID: {}", employeeId);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found for this id : " + employeeId);
                });
    }

    /**
     * Save a new employee.
     *
     * @param employee The employee to be saved.
     * @return The saved employee.
     */
    public Employee saveNewEmployee(Employee employee){
        logger.info("Saving new employee: {}", employee);
        return employeeRepository.save(employee);
    }

    /**
     * Update an existing employee.
     *
     * @param employeeId      The ID of the employee to update.
     * @param updatedEmployee The updated employee object.
     * @return The updated employee.
     * @throws ResponseStatusException If no employee with the given ID is found.
     */
    public Employee updateEmployee(Long employeeId, Employee updatedEmployee ){
        logger.info("Updating employee with ID: {}", employeeId);
        return employeeRepository.findById(employeeId)
                .map(existingEmployee -> {
                    logger.info("Found employee with ID: {}. Updating details.", employeeId);
                    existingEmployee.setFirstName(updatedEmployee.getFirstName());
                    existingEmployee.setLastName(updatedEmployee.getLastName());
                    existingEmployee.setDepartment(updatedEmployee.getDepartment());
                    existingEmployee.setEmail(updatedEmployee.getEmail());
                    existingEmployee.setTasks(updatedEmployee.getTasks());
                    return employeeRepository.save(existingEmployee);
                })
                .orElseThrow(() -> {
                    logger.error("Employee not found for ID: {}", employeeId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found for this id : " + employeeId);
                });
    }

    /**
     * Delete an employee by ID.
     *
     * @param employeeId The ID of the employee to delete.
     */
    public void deleteEmployee(Long employeeId){
        logger.info("Deleting employee with ID: {}", employeeId);
        employeeRepository.deleteById(employeeId);
    }

    /**
     * Retrieve employees grouped by department.
     *
     * @return A map containing departments as keys and lists of employees as values.
     */
    public Map<String, List<Employee>> getEmployeesByDepartment() {
        logger.info("Fetching employees grouped by department");
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
    }

    /**
     * Retrieve employees grouped by task.
     *
     * @return A map containing task IDs as keys and lists of employees as values.
     */
    public Map<Long, List<Employee>> getEmployeesByTask() {
        logger.info("Fetching employees grouped by tasks");
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .flatMap(employee -> employee.getTasks().stream().map(task -> Map.entry(task.getTaskId(), employee)))
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
    }
}
