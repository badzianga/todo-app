package com.badzianga.todo.repository;

import com.badzianga.todo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsByTitle(String title);
    List<Task> findByDone(boolean done);
}
