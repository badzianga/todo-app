package com.badzianga.todo.service;

import com.badzianga.todo.exception.InvalidUserException;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public Page<Task> getTasks(UserDetails userDetails, Pageable pageable, Boolean done, String title) {
        User user = userRepository.findByEmailIgnoreCase(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if (done == null && title == null) {
            return taskRepository.findByUser(user, pageable);
        }
        if (done != null && title != null) {
            return taskRepository.findByUserAndDoneAndTitleContainingIgnoreCase(user, done, title, pageable);
        }
        return done != null
                ? taskRepository.findByUserAndDone(user, done, pageable)
                : taskRepository.findByUserAndTitleContainingIgnoreCase(user, title, pageable);
    }

    public Task getTask(UserDetails user, Long id) throws TaskNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
        if (!task.getUser().getEmail().equals(user.getUsername())) {
            throw new InvalidUserException();
        }
        return task;
    }

    public Task addTask(UserDetails userDetails, AddTaskRequest request) {
        User user = userRepository.findByEmailIgnoreCase(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);
        Task task = new Task(request.getTitle(), request.getDescription(), user);
        return taskRepository.save(task);
    }

    public Task updateTask(UserDetails userDetails, UpdateTaskRequest request, Long id)
            throws TaskNotFoundException, InvalidUserException {
        User user = userRepository.findByEmailIgnoreCase(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);
        return taskRepository.findById(id)
                .map(task -> {
                    if (!task.getUser().getEmail().equals(user.getEmail())) {
                        throw new InvalidUserException();
                    }
                    return updateExistingTask(task, request);
                })
                .map(taskRepository::save)
                .orElseThrow(TaskNotFoundException::new);
    }

    public Task updateTaskStatus(UserDetails user, Long id)
            throws TaskNotFoundException, InvalidUserException {
        return taskRepository.findById(id)
                .map(task -> {
                    if (!task.getUser().getEmail().equals(user.getUsername())) {
                        throw new InvalidUserException();
                    }
                    return swapTaskStatus(task);
                })
                .map(taskRepository::save)
                .orElseThrow(TaskNotFoundException::new);
    }

    public void deleteTask(UserDetails user, Long id) throws TaskNotFoundException, InvalidUserException {
        taskRepository.findById(id).ifPresentOrElse(task -> {
            if (!task.getUser().getEmail().equals(user.getUsername())) {
                throw new InvalidUserException();
            }
            taskRepository.delete(task);
        }, () -> {
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
