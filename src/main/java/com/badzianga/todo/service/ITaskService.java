package com.badzianga.todo.service;

import com.badzianga.todo.model.Task;
import com.badzianga.todo.request.AddTaskRequest;
import com.badzianga.todo.request.UpdateTaskRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITaskService {
    Page<Task> getTasks(Pageable pageable, Boolean done, String title);
    Task getTask(Long id);
    Task addTask(String email, AddTaskRequest request);
    Task updateTask(UpdateTaskRequest request, Long id);
    Task updateTaskStatus(Long id);
    void deleteTask(Long id);
}
