package com.badzianga.todo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@NoArgsConstructor
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private boolean done;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Task(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.done = false;
        this.updatedAt = this.createdAt = LocalDateTime.now();
        this.user = user;
    }

    public void update() {
        updatedAt = LocalDateTime.now();
    }
}
