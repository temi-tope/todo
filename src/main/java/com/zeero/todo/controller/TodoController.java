package com.zeero.todo.controller;

import com.zeero.todo.entity.Task;
import com.zeero.todo.enums.Priority;
import com.zeero.todo.service.TaskService;
import com.zeero.todo.util.TodoUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/tasks")
@Validated
@RequiredArgsConstructor
public class TodoController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Object> createTask(@Valid @RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return TodoUtil.buildSuccessResponse(createdTask);
    }

    @GetMapping
    public ResponseEntity<Page<Task>> getTasks(
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) String dueDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dueDate") String sortBy) {

        LocalDate dueDateParsed = dueDate != null ? LocalDate.parse(dueDate, DateTimeFormatter.ISO_DATE) : null;


        Page<Task> tasks = taskService.getFilteredAndSortedTasks(priority, dueDateParsed, page, size, sortBy);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskCode}")
    public ResponseEntity<Object> getTaskByCode(@PathVariable String taskCode) {
        Task task = taskService.getTaskByCode(taskCode);
        return TodoUtil.buildSuccessResponse(task);
    }

    @PutMapping("/{taskCode}")
    public ResponseEntity<Object> updateTask(@PathVariable String taskCode, @Valid @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(taskCode, task);
        return TodoUtil.buildSuccessResponse(updatedTask);
    }

    @DeleteMapping("/{taskCode}")
    public ResponseEntity<Object> deleteTask(@PathVariable String taskCode) {
        taskService.deleteTask(taskCode);
        return TodoUtil.buildSuccessResponse("Task has been deleted");
    }
}
