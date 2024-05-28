package com.demo.controller;


import com.demo.model.Task;
import com.demo.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/task")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        logger.info("Request received to fetch all tasks");
        List<Task> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        logger.info("Request received to fetch task with ID: {}", taskId);
        Task task = taskService.getTaskById(taskId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Task> createNewTask(@RequestBody Task task) {
        logger.info("Request received to create a new task: {}", task);
        Task task1 = taskService.saveNewTask(task);
        return new ResponseEntity<>(task1,HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task task) {
        logger.info("Request received to update task with ID: {}", taskId);
        try {
            Task updatedTask = taskService.updateTask(taskId, task);
            logger.info("Task with ID: {} successfully updated", taskId);
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            logger.error("Error updating task with ID: {}. {}", taskId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        logger.info("Request received to delete task with ID: {}", taskId);
        taskService.deleteTask(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Handles the POST request to upload and save tasks from a JSON file.
     *
     * @param tasks the list of tasks to be saved.
     * @return a ResponseEntity with an HTTP status code.
     */
    @PostMapping("/file")
    public ResponseEntity<Void> readJson(@RequestBody List<Task> tasks) {
        logger.info("Request received to create a new tasks from Json File: {}", tasks);
        taskService.saveTasks(tasks);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
