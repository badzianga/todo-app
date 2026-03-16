package com.badzianga.todo.request;

import lombok.Data;

@Data
public class UpdateTaskRequest {
    private String title;
    private String description;
    private boolean done;
}
