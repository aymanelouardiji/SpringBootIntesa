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

    /**
     * Constructor for TaskService.
     *
     * @param taskRepository The repository for accessing task data.
     */
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Retrieve all tasks.
     *
     * @return A list of all tasks.
     */
    public List<Task> getAllTasks(){
        logger.info("Fetching all tasks");
        return taskRepository.findAll();
    }

    /**
     * Retrieve a task by ID.
     *
     * @param taskId The ID of the task to retrieve.
     * @return The task with the specified ID.
     * @throws ResponseStatusException If no task with the given ID is found.
     */
    public Task getTaskById(Long taskId){
        logger.info("Fetching task with ID: {}", taskId);
        return taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    logger.error("Task not found for ID: {}", taskId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found for this id : " + taskId);
                });

    }

    /**
     * Save a new task.
     *
     * @param task The task to be saved.
     * @return The saved task.
     */
    public Task saveNewTask(Task task){
        logger.info("Saving new task: {}", task);
        return taskRepository.save(task);
    }

    /**
     * Update an existing task.
     *
     * @param taskId       The ID of the task to update.
     * @param updatedTask The updated task object.
     * @return The updated task.
     * @throws ResponseStatusException If no task with the given ID is found.
     */
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

    /**
     * Delete a task by ID.
     *
     * @param taskId The ID of the task to delete.
     */
    public void deleteTask(Long taskId){
        logger.info("Deleting task with ID: {}", taskId);
        taskRepository.deleteById(taskId);
    }
}
