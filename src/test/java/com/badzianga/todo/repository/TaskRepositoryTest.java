package com.badzianga.todo.repository;

import com.badzianga.todo.model.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
public class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void shouldCheckIfTaskExistsByTitle() {
        Task task = new Task("title", "description");
        taskRepository.save(task);

        boolean found = taskRepository.existsByTitle("title");
        boolean notFound = taskRepository.existsByTitle("something");

        Assertions.assertThat(found).isTrue();
        Assertions.assertThat(notFound).isFalse();
    }
}
