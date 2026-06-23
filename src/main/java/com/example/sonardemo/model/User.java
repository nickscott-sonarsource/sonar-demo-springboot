package com.example.sonardemo.model;

public class User {

    // Code smell: public mutable fields instead of encapsulation
    public Long id;
    public String username;
    public String email;

    public User() {
    }

    public User(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
