package com.zeero.todo.service;

import com.zeero.todo.entity.Task;
import com.zeero.todo.enums.Priority;
import com.zeero.todo.exception.ResourceNotFoundException;
import com.zeero.todo.repository.TaskRepository;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        Task task = new Task();
        task.setTitle("Sample Task");

        when(taskRepository.save(task)).thenReturn(task);

        Task createdTask = taskService.createTask(task);
        System.out.println(createdTask.toString());
        assertNotNull(createdTask);
        assertEquals("Sample Task", createdTask.getTitle());
    }

    @Test
    public void testGetFilteredAndSortedTasks() {
        // Given
        Task task1 = new Task();
        task1.setId("1L");
        task1.setTitle("Task 1");
        task1.setPriority(Priority.HIGH);
        task1.setDueDate(LocalDate.of(2024, 11, 5));
        task1.setActive(true);

        Task task2 = new Task();
        task2.setId("2L");
        task2.setTitle("Task 2");
        task2.setPriority(Priority.LOW);
        task2.setDueDate(LocalDate.of(2024, 11, 6));
        task2.setActive(true);

        List<Task> tasks = Arrays.asList(task1, task2);
        Page<Task> taskPage = new PageImpl<>(tasks);

        // Mock the behavior of the repository
        when(taskRepository.findAll((Specification<Task>) any(), any(Pageable.class))).thenReturn(taskPage);

        // When
        Page<Task> result = taskService.getFilteredAndSortedTasks(Priority.HIGH, LocalDate.of(2024, 11, 5), 0, 10, "dueDate");

        // Then
        assertEquals(2, result.getTotalElements()); // Expecting 2 tasks in total
        assertEquals(2, result.getContent().size()); // Expecting 2 tasks in content

        // Verify that the repository method was called with the correct parameters
        verify(taskRepository, times(1)).findAll((Specification<Task>) any(), any(Pageable.class));
    }

    @Test
    public void testGetFilteredAndSortedTasks_NoTasksFound() {
        // Given: No tasks in the repository
        Page<Task> emptyTaskPage = new PageImpl<>(Collections.emptyList());

        // Mock the behavior of the repository
        when(taskRepository.findAll((Specification<Task>) any(), any(Pageable.class))).thenReturn(emptyTaskPage);

        // When
        Page<Task> result = taskService.getFilteredAndSortedTasks(Priority.HIGH, LocalDate.of(2024, 11, 5), 0, 10, "dueDate");

        // Then
        assertEquals(0, result.getTotalElements()); // Expecting 0 tasks in total
        assertEquals(0, result.getContent().size()); // Expecting no tasks in content

        // Verify that the repository method was called with the correct parameters
        verify(taskRepository, times(1)).findAll((Specification<Task>) any(), any(Pageable.class));
    }

    @Test
    void testGetTaskById_Found() {
        Task task = new Task();
        task.setTaskCode("TSK-1234567");
        task.setTitle("Sample Task");

        when(taskRepository.findByTaskCodeAndActive("TSK-1234567")).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTaskByCode("TSK-1234567");
        assertNotNull(foundTask);
        assertEquals("Sample Task", foundTask.getTitle());
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskRepository.findByTaskCodeAndActive("TSK-1234567")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskByCode("TSK-1234567"));
    }

    @Test
    void testUpdateTask() {
        Task existingTask = new Task();
        existingTask.setTaskCode("TSK-1234567");
        existingTask.setTitle("Old Task");

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");

        when(taskRepository.findByTaskCodeAndActive("TSK-1234567")).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.updateTask("TSK-1234567", updatedTask);
        assertEquals("Updated Task", result.getTitle());
    }

    @Test
    void testDeleteTask() {
        Task task = new Task();
        task.setTaskCode("TSK-1234567");
        task.setActive(true);

        when(taskRepository.findByTaskCodeAndActive("TSK-1234567")).thenReturn(Optional.of(task));
        taskService.deleteTask("TSK-1234567");

        verify(taskRepository, times(1)).save(task);
        assertFalse(task.isActive());
    }
}
