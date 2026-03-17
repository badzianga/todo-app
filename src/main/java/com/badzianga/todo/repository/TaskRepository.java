package com.badzianga.todo.repository;

import com.badzianga.todo.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByDone(Pageable pageable, boolean done);
    Page<Task> findByTitleContainingIgnoreCase(Pageable pageable, String title);
    Page<Task> findByDoneAndTitleContainingIgnoreCase(Pageable pageable, boolean done, String title);
}
