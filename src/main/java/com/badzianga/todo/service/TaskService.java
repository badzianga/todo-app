package com.badzianga.todo.service;

import com.badzianga.todo.model.Task;
import com.badzianga.todo.repository.TaskRepository;
import com.badzianga.todo.request.AddTaskRequest;
import com.badzianga.todo.request.UpdateTaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Task getTask(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public Task addTask(AddTaskRequest request) {
        if (taskRepository.existsByTitle(request.getTitle())) {
            throw new RuntimeException("Task with given title already exists");
        }

        Task task = new Task(request.getTitle(), request.getDescription());

        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(UpdateTaskRequest request, Long id) {
        return taskRepository.findById(id)
                .map(task -> updateExistingTask(task, request))
                .map(taskRepository::save)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    private Task updateExistingTask(Task task, UpdateTaskRequest request) {
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDone(request.isDone());
        task.update();
        return task;
    }
}
