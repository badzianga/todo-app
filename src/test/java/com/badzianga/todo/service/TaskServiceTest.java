package com.badzianga.todo.service;

import com.badzianga.todo.exception.TaskNotFoundException;
import com.badzianga.todo.request.AddTaskRequest;
import org.assertj.core.api.Assertions;
import com.badzianga.todo.model.Task;
import com.badzianga.todo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class TaskServiceTest {
    private TaskRepository taskRepository;
    private ITaskService taskService;

    @BeforeEach
    public void setup() {
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    public void shouldGetAllTasks() {
        Task task1 = new Task("title1", "description1");
        Task task2 = new Task("title2", "description2");
        Task task3 = new Task("title3", "description3");

        Mockito.when(taskRepository.findAll()).thenReturn(List.of(task1, task2, task3));

        List<Task> tasks = taskService.getTasks(null, null);

        Mockito.verify(taskRepository, Mockito.times(1)).findAll();
        Assertions.assertThat(tasks).hasSize(3);
        Assertions.assertThat(tasks.get(0)).isEqualTo(task1);
        Assertions.assertThat(tasks.get(1)).isEqualTo(task2);
        Assertions.assertThat(tasks.get(2)).isEqualTo(task3);
    }

    @Test
    public void shouldGetTaskById() {
        Long id = 1L;
        Task task1 = new Task("title1", "description1");
        task1.setId(id);

        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(task1));

        Task task = taskService.getTask(id);

        Assertions.assertThat(task).isEqualTo(task1);
        Mockito.verify(taskRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void shouldThrowExceptionIfTaskNotFound() {
        Long id = 1L;
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> taskService.getTask(id))
                .isInstanceOf(TaskNotFoundException.class);
        Mockito.verify(taskRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void shouldAddTask() {
        String title = "title";
        String description = "description";
        AddTaskRequest request = new AddTaskRequest();
        request.setTitle(title);
        request.setDescription(description);
        Task task = new Task(title, description);

        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        Task addedTask = taskService.addTask(request);

        Assertions.assertThat(addedTask).isNotNull();
        Assertions.assertThat(addedTask.getTitle()).isEqualTo(title);
        Assertions.assertThat(addedTask.getDescription()).isEqualTo(description);
        Assertions.assertThat(addedTask.getCreatedAt()).isNotNull();
        Assertions.assertThat(addedTask.getUpdatedAt()).isNotNull();
        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.any(Task.class));
    }
}
