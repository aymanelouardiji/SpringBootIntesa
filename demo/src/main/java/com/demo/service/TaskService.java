package com.demo.service;


import com.demo.model.Task;
import com.demo.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;


@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks(){
        logger.info("Fetching all tasks");
        return taskRepository.findAll();
    }

    public Task getTaskById(Long taskId){
        logger.info("Fetching task with ID: {}", taskId);
        return taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    logger.error("Task not found for ID: {}", taskId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found for this id : " + taskId);
                });

    }
    public Task saveNewTask(Task task){
        logger.info("Saving new task: {}", task);
        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, Task updatedTask) {
        logger.info("Updating task with ID: {}", taskId);
        return taskRepository.findById(taskId)
                .map(existingTask -> {
                    logger.info("Found task with ID: {}. Updating details.", taskId);
                    existingTask.setTaskName(updatedTask.getTaskName());
                    existingTask.setDescription(updatedTask.getDescription());
                    existingTask.setDeadline(updatedTask.getDeadline());
                    return taskRepository.save(existingTask);
                })
                .orElseThrow(() -> {
                    logger.error("Task not found for ID: {}", taskId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found for this id: " + taskId);
                });
    }
    public void deleteTask(Long taskId){
        logger.info("Deleting task with ID: {}", taskId);
        taskRepository.deleteById(taskId);
    }
}
