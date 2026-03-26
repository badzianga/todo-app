package com.badzianga.todo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
