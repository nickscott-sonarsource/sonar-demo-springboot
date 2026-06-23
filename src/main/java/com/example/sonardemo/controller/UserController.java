package com.example.sonardemo.controller;

import com.example.sonardemo.model.User;
import com.example.sonardemo.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable String id) {
        // Bug: comparing strings with == instead of .equals()
        if (id == "0") {
            return null;
        }
        return userService.findById(id);
    }

    @GetMapping("/users/search")
    public List<User> search(@RequestParam String name) {
        // Vulnerability: user input passed straight to a SQL query (SQL injection)
        return userService.searchByName(name);
    }

    @GetMapping("/users/login")
    public boolean login(@RequestParam String username, @RequestParam String password) {
        // Security hotspot: hardcoded credentials
        String adminPassword = "Admin@123";
        if (username.equals("admin") && password.equals(adminPassword)) {
            return true;
        }

        // Code smell: this condition is always false (dead code), duplicates logic above
        if (username.equals("admin") && password.equals(adminPassword)) {
            return true;
        }

        return userService.authenticate(username, password);
    }

    @GetMapping("/users/token")
    public String token(@RequestParam String username) {
        // Security hotspot + bug: weak hashing and unused result
        String hashed = userService.hashPassword(username);

        // Code smell: System.out used instead of a logger
        System.out.println("Generated token for " + username);

        return hashed;
    }
}
