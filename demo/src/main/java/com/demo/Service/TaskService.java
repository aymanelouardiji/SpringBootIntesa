package com.demo.Service;


import com.demo.Model.Task;
import com.demo.Repository.TaskRepository;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    public List<Task> getAllTasks(){return taskRepository.findAll();}

    public Task getTaskById(Long taskId){
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found for this id : " + taskId));

    }
    public Task saveNewTask(Task task){return taskRepository.save(task);}

    public Optional<Task> updateTask(Long taskId, Task updatedTask){
        return taskRepository.findById(taskId)
        .map(existingTask -> {
        existingTask.setTaskName(updatedTask.getTaskName());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setDeadline(updatedTask.getDeadline());
        return taskRepository.save(existingTask);
        });
    }
    public void deleteTask(Long taskId){
        taskRepository.deleteById(taskId);
    }
}
