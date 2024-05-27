package com.demo.Service;

import com.demo.Model.Employee;
import com.demo.Repository.EmployeeRepository;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long employeeId){
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found for this id : " + employeeId));
    }

    public Employee saveNewEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    public Optional <Employee> updateEmployee(Long employeeId, Employee updatedEmployee ){
       return employeeRepository.findById(employeeId)
       .map(existingEmployee -> {
       existingEmployee.setFirstName(updatedEmployee.getFirstName());
       existingEmployee.setLastName(updatedEmployee.getLastName());
       existingEmployee.setDepartment(updatedEmployee.getDepartment());
       existingEmployee.setEmail(updatedEmployee.getEmail());
       existingEmployee.setTasks(updatedEmployee.getTasks());
       return employeeRepository.save(existingEmployee);
        });
    }
    public void deleteEmployee(Long employeeId){
        employeeRepository.deleteById(employeeId);
    }
}
