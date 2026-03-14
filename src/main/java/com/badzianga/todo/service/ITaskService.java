package com.badzianga.todo.service;

import com.badzianga.todo.model.Task;
import com.badzianga.todo.request.AddTaskRequest;

import java.util.List;

public interface ITaskService {
    List<Task> getTasks();
    Task addTask(AddTaskRequest request);
}
