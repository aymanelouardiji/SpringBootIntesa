package com.demo.service;

import com.demo.model.Task;
import com.demo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TaskServiceTest {


    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        task = new Task(1L, "Task 1", "Description 1", "2024-05-27");
    }

    @Test
    void testGetAllTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(1, result.size());
    }

    @Test
    void testGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertEquals("Task 1", result.getTaskName());
    }

    @Test
    void testSaveNewTask() {
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.saveNewTask(task);

        assertEquals(task, result);
    }

    @Test
    void testUpdateTask() {
        Task updatedTask = new Task(1L, "Task 1 Updated", "Description 1 Updated", "2024-06-20");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(updatedTask);

        Task result = taskService.updateTask(1L, updatedTask);

        // Verify the result
        assertEquals("Task 1 Updated", result.getTaskName());
        assertEquals("Description 1 Updated", result.getDescription());
        assertEquals("2024-06-20", result.getDeadline());
    }

    @Test
    void testUpdateTaskNotFound() {
        Task updatedTask = new Task(1L, "Task 1 Updated", "Description 1 Updated", "2024-06-20");

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> taskService.updateTask(1L, updatedTask));
    }

    @Test
    void testDeleteTask() {
        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> taskService.getTaskById(1L));
    }
}
