package com.badzianga.todo.repository;

import com.badzianga.todo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByDone(boolean done);
    List<Task> findByTitleContainingIgnoreCase(String title);
    List<Task> findByDoneAndTitleContainingIgnoreCase(boolean done, String title);
}
