package com.zeero.todo.util;

import com.zeero.todo.entity.Task;
import com.zeero.todo.enums.Priority;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TaskSpecification {
    private TaskSpecification() {
        throw new IllegalStateException("Utility class");
    }
    public static Specification<Task> withPriority(Priority priority) {
        return (root, query, criteriaBuilder) -> {
            if (priority == null) {
                return criteriaBuilder.conjunction(); // No filter
            }
            return criteriaBuilder.equal(root.get("priority"), priority);
        };
    }

    public static Specification<Task> withDueDate(LocalDate dueDate) {
        return (root, query, criteriaBuilder) -> {
            if (dueDate == null) {
                return criteriaBuilder.conjunction(); // No filter
            }
            return criteriaBuilder.equal(root.get("dueDate"), dueDate);
        };
    }

    public static Specification<Task> isActive() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("active"));
    }
}
