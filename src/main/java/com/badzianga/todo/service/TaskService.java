package com.badzianga.todo.service;

import com.badzianga.todo.exception.TaskNotFoundException;
import com.badzianga.todo.exception.UserNotFoundException;
import com.badzianga.todo.model.Task;
import com.badzianga.todo.model.User;
import com.badzianga.todo.repository.TaskRepository;
import com.badzianga.todo.repository.UserRepository;
import com.badzianga.todo.request.AddTaskRequest;
import com.badzianga.todo.request.UpdateTaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public Page<Task> getTasks(Pageable pageable, Boolean done, String title) {
        if (done == null && title == null) {
            return taskRepository.findAll(pageable);
        }
        if (done != null && title != null) {
            return taskRepository.findByDoneAndTitleContainingIgnoreCase(pageable, done, title);
        }
        return done != null
                ? taskRepository.findByDone(pageable, done)
                : taskRepository.findByTitleContainingIgnoreCase(pageable, title);
    }

    @Override
    public Task getTask(Long id) throws TaskNotFoundException {
        return taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
    }

    @Override
    public Task addTask(String email, AddTaskRequest request) {
        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow(UserNotFoundException::new);
        Task task = new Task(request.getTitle(), request.getDescription(), user);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(UpdateTaskRequest request, Long id) throws TaskNotFoundException {
        return taskRepository.findById(id)
                .map(task -> updateExistingTask(task, request))
                .map(taskRepository::save)
                .orElseThrow(TaskNotFoundException::new);
    }

    @Override
    public Task updateTaskStatus(Long id) throws TaskNotFoundException {
        return taskRepository.findById(id)
                .map(this::swapTaskStatus)
                .map(taskRepository::save)
                .orElseThrow(TaskNotFoundException::new);
    }

    @Override
    public void deleteTask(Long id) throws TaskNotFoundException {
        taskRepository.findById(id).ifPresentOrElse(taskRepository::delete, () -> {
            throw new TaskNotFoundException();
        });
    }

    private Task updateExistingTask(Task task, UpdateTaskRequest request) {
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDone(request.isDone());
        task.update();
        return task;
    }

    private Task swapTaskStatus(Task task) {
        task.setDone(!task.isDone());
        task.update();
        return task;
    }
}
