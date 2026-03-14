package com.badzianga.todo.request;

import lombok.Data;

@Data
public class AddTaskRequest {
    private String title;
    private String description;
}
