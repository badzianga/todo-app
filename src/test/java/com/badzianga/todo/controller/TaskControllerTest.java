package com.badzianga.todo.controller;

import com.badzianga.todo.model.Task;
import com.badzianga.todo.request.AddTaskRequest;
import com.badzianga.todo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private final TaskService taskService = Mockito.mock(TaskService.class);

    @Value("/api/v1/tasks")
    private String url;

    @Test
    public void shouldReturnAllTasks() throws Exception {
        Mockito.when(taskService.getTasks()).thenReturn(List.of(
                new Task("title1", "description1"),
                new Task("title2", "description2")
        ));

        mvc.perform(MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("title1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].description").value("description1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].title").value("title2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].description").value("description2"));
    }

    @Test
    public void shouldAddTask() throws Exception {
        AddTaskRequest request = new AddTaskRequest();
        request.setTitle("title");
        request.setDescription("description");
        Task task = new Task(request.getTitle(), request.getDescription());
        Mockito.when(taskService.addTask(request)).thenReturn(task);

        mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(task.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value(task.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.done").value(task.isDone()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.createdAt").value(task.getCreatedAt().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.updatedAt").value(task.getUpdatedAt().toString()));
    }

    @Test
    public void shouldThrowExceptionWhenAddingTaskWithExistingName() throws Exception {
        Mockito.when(taskService.addTask(Mockito.any())).thenThrow(new RuntimeException("error message"));

        mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddTaskRequest())))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("error message"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());
    }
}
