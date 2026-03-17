package com.badzianga.todo.service;

import com.badzianga.todo.model.Task;
import com.badzianga.todo.request.AddTaskRequest;
import com.badzianga.todo.request.UpdateTaskRequest;

import java.util.List;

public interface ITaskService {
    List<Task> getTasks();
    Task getTask(Long id);
    Task addTask(AddTaskRequest request);
    Task updateTask(UpdateTaskRequest request, Long id);
    Task updateTaskStatus(Long id);
    void deleteTask(Long id);
}
