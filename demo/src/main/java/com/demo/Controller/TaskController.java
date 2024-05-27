package com.demo.Controller;


import com.demo.Model.Employee;
import com.demo.Model.Task;
import com.demo.Service.EmployeeService;
import com.demo.Service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        List<Task> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Task> createNewTask(@RequestBody Task task) {
        Task task1 = taskService.saveNewTask(task);
        return new ResponseEntity<>(task1,HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task task) {
        Optional<Task> updatedEmployee = taskService.updateTask(taskId, task);
        return updatedEmployee.map(ResponseEntity::ok)
                .orElseGet(() -> (ResponseEntity<Task>) ResponseEntity.notFound());
    }

}
