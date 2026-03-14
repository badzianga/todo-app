package com.badzianga.todo.service;

import com.badzianga.todo.model.Task;
import com.badzianga.todo.repository.TaskRepository;
import com.badzianga.todo.request.AddTaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {
    private final TaskRepository taskRepository;

    @Override
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task addTask(AddTaskRequest request) {
        if (taskRepository.existsByTitle(request.getTitle())) {
            throw new RuntimeException("Task with given title already exists");
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDone(false);

        LocalDateTime createdAt = LocalDateTime.now();
        task.setCreatedAt(createdAt);
        task.setUpdatedAt(createdAt);

        return taskRepository.save(task);
    }
}
