package com.badzianga.todo.controller;

import com.badzianga.todo.exception.TaskNotFoundException;
import com.badzianga.todo.model.Task;
import com.badzianga.todo.request.AddTaskRequest;
import com.badzianga.todo.request.UpdateTaskRequest;
import com.badzianga.todo.response.ApiResponse;
import com.badzianga.todo.service.ITaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final ITaskService taskService;

    @GetMapping
    public ResponseEntity<ApiResponse> getTasks(@PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC)
                                                    Pageable pageable,
                                                @RequestParam(required = false) Boolean done,
                                                @RequestParam(required = false) String title) {
        Page<Task> tasks = taskService.getTasks(pageable, done, title);
        return ResponseEntity.ok(new ApiResponse("Success", tasks));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTask(@PathVariable Long id) {
        try {
            Task task = taskService.getTask(id);
            return ResponseEntity.ok(new ApiResponse("Success", task));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createTask(@Valid @RequestBody AddTaskRequest request) {
        Task task = taskService.addTask(request);
        return ResponseEntity.ok(new ApiResponse("Success", task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request) {
        try {
            Task task = taskService.updateTask(request, id);
            return ResponseEntity.ok(new ApiResponse("Success", task));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTaskStatus(@PathVariable Long id) {
        try {
            Task task = taskService.updateTaskStatus(id);
            return ResponseEntity.ok(new ApiResponse("Success", task));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok(new ApiResponse("Success", null));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
