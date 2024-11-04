package com.zeero.todo.controller;

import com.zeero.todo.entity.Task;
import com.zeero.todo.enums.Priority;
import com.zeero.todo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setup() {
        taskRepository.deleteAll();
    }

    @Test
    void testCreateTask() throws Exception {
        String taskJson = "{\"title\":\"New Task\",\"description\":\"Task Description\",\"dueDate\":\"2024-12-31\",\"priority\":\"HIGH\"}";

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("New Task")))
                .andExpect(jsonPath("$.data.priority", is(Priority.HIGH.toString())));
    }

    @Test
    void testGetTasks() throws Exception {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setTaskCode("TSK-1234567");
        task1.setDescription("Description 1");
        task1.setDueDate(LocalDate.now().plusDays(1));
        task1.setPriority(Priority.HIGH);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setTaskCode("TSK-1234500");
        task2.setDescription("Description 2");
        task2.setDueDate(LocalDate.now().plusDays(2));
        task2.setPriority(Priority.LOW);

        taskRepository.save(task1);
        taskRepository.save(task2);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(2)));
    }

    @Test
    void testGetTaskById() throws Exception {
        Task task = new Task();
        task.setTitle("Sample Task");
        task.setTaskCode("TSK-1234567");
        task.setDescription("Sample Description");
        task.setDueDate(LocalDate.now().plusDays(1));
        task.setPriority(Priority.MEDIUM);
        Task savedTask = taskRepository.save(task);

        mockMvc.perform(get("/api/tasks/{taskCode}", savedTask.getTaskCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("Sample Task")))
                .andExpect(jsonPath("$.data.priority", is(Priority.MEDIUM.toString())));
    }

    @Test
    void testUpdateTask() throws Exception {
        Task task = new Task();
        task.setTitle("Old Task");
        task.setTaskCode("TSK-1234567");
        task.setDescription("Old Description");
        task.setDueDate(LocalDate.now().plusDays(1));
        task.setPriority(Priority.LOW);
        Task savedTask = taskRepository.save(task);

        String updatedTaskJson = "{\"title\":\"Updated Task\",\"description\":\"Updated Description\",\"dueDate\":\"2024-12-01\",\"priority\":\"HIGH\"}";

        mockMvc.perform(put("/api/tasks/{taskCode}", savedTask.getTaskCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTaskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("Updated Task")))
                .andExpect(jsonPath("$.data.priority", is(Priority.HIGH.toString())));
    }

    @Test
    void testDeleteTask() throws Exception {
        Task task = new Task();
        task.setTitle("Task to delete");
        task.setTaskCode("TSK-1234567");
        task.setDescription("Old Description");
        task.setDueDate(LocalDate.now().plusDays(1));
        task.setPriority(Priority.LOW);
        Task savedTask = taskRepository.save(task);

        mockMvc.perform(delete("/api/tasks/{taskCode}", savedTask.getTaskCode()))
                .andExpect(status().isOk());

        Task deletedTask = taskRepository.findByTaskCode(savedTask.getTaskCode()).orElse(null);
        assertNotNull(deletedTask);
        assertFalse(deletedTask.isActive());
    }
}
