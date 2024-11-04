package com.zeero.todo.repository;

import com.zeero.todo.entity.Task;
import com.zeero.todo.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, String>, JpaSpecificationExecutor<Task> {
    List<Task> findByActive(boolean active);
    @Query("SELECT t FROM Task t WHERE t.taskCode = :taskCode AND t.active = true")
    Optional<Task> findByTaskCodeAndActive(String taskCode);
    Optional<Task>findByTaskCode(String taskCode);
    List<Task> findByActiveAndPriority(boolean active, Priority priority);
}
