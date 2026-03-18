package com.badzianga.todo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateTaskRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must have 3-100 characters")
    private String title;

    @Size(max = 500, message = "Description too long")
    private String description;

    private boolean done;
}
