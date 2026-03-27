package com.badzianga.todo.repository;

import com.badzianga.todo.model.Task;
import com.badzianga.todo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByUser(User user, Pageable pageable);
    Page<Task> findByUserAndDone(User user, boolean done, Pageable pageable);
    Page<Task> findByUserAndTitleContainingIgnoreCase(User user, String title, Pageable pageable);
    Page<Task> findByUserAndDoneAndTitleContainingIgnoreCase(User user, boolean done, String title, Pageable pageable);
}
