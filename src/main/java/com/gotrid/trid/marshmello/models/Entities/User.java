package com.gotrid.trid.marshmello.models.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor


public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    // Constructors, getters, setters

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    // Add these methods to your User.java entity class
    public boolean isClient() {
        if (this.roles == null) {
            return false;
        }

        return this.roles.stream()
                .map(role -> role.getName().toUpperCase().trim())
                .anyMatch(roleName -> roleName.equals("ROLE_CLIENT"));
    }

    public boolean isAdmin() {
        if (this.roles == null) {
            return false;
        }

        return this.roles.stream()
                .map(role -> role.getName().toUpperCase().trim())
                .anyMatch(roleName -> roleName.equals("ROLE_ADMIN"));
    }
}

