package com.zeero.todo.entity;

import com.zeero.todo.enums.Priority;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
public class Task {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;

    @Column(unique = true)
    private String taskCode;

    @NotBlank(message = "Title is required and cannot be empty")
    private String title;

    private String description;

    @FutureOrPresent(message = "Due date must be today or in the future")
    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    @NotNull(message = "Priority is required")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    private boolean completed = false;

    private boolean active = true;
}
