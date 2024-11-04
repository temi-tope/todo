package com.zeero.todo.service;

import com.zeero.todo.entity.Task;
import com.zeero.todo.enums.Priority;
import com.zeero.todo.exception.ResourceNotFoundException;
import com.zeero.todo.repository.TaskRepository;
import com.zeero.todo.util.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;


import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task createTask(Task task) {
        task.setTaskCode(generateTaskCode());
        return taskRepository.save(task);
    }

    public Page<Task> getFilteredAndSortedTasks(Priority priority, LocalDate dueDate, int page, int size, String sortBy) {
        Specification<Task> spec = Specification.where(TaskSpecification.isActive())
                .and(TaskSpecification.withPriority(priority))
                .and(TaskSpecification.withDueDate(dueDate));

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return taskRepository.findAll(spec, pageable);
    }

    public Task getTaskByCode(String taskCode) {
        return taskRepository.findByTaskCodeAndActive(taskCode)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskCode));
    }

    public Task updateTask(String taskCode, Task updatedTask) {
        Task existingTask = getTaskByCode(taskCode);
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setDueDate(updatedTask.getDueDate());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setCompleted(updatedTask.isCompleted());
        return taskRepository.save(existingTask);
    }

    public void deleteTask(String taskCode) {
        Task task = getTaskByCode(taskCode);
        task.setActive(false);
        taskRepository.save(task);
    }

    public String generateTaskCode() {
        String taskPrefix = "TSK-";
        return taskPrefix.concat(RandomStringUtils.randomNumeric(7));
    }
}
