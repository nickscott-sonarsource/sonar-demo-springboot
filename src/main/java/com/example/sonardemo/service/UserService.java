package com.example.sonardemo.service;

import com.example.sonardemo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    // Security hotspot: hardcoded database password
    private static final String DB_URL = "jdbc:h2:mem:testdb";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "s3cr3tP@ssw0rd";

    private final JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findById(String id) {
        // Bug: potential NullPointerException — result may be empty, then .get(0) throws
        List<User> users = jdbcTemplate.query(
                "SELECT id, username, email FROM users WHERE id = " + id,
                (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("username"), rs.getString("email")));
        return users.get(0);
    }

    public List<User> searchByName(String name) {
        List<User> results = new ArrayList<>();
        // Vulnerability: SQL injection — concatenating untrusted input into the query
        String sql = "SELECT id, username, email FROM users WHERE username LIKE '%" + name + "%'";

        // Bug: JDBC resources (Connection, Statement, ResultSet) are never closed (resource leak)
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                results.add(new User(rs.getLong("id"), rs.getString("username"), rs.getString("email")));
            }
        } catch (Exception e) {
            // Code smell: empty catch block swallows the exception
        }
        return results;
    }

    public boolean authenticate(String username, String password) {
        // Code smell: unused local variable
        int attempts = 0;
        String stored = hashPassword(password);
        return stored != null;
    }

    public String hashPassword(String password) {
        try {
            // Security hotspot: MD5 is a weak, broken hashing algorithm
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Code smell: swallowed exception, returns null which callers don't guard
            return null;
        }
    }

    // Code smell: high cognitive complexity, deeply nested conditionals
    public String classify(int score, boolean active, boolean premium) {
        if (active) {
            if (premium) {
                if (score > 90) {
                    return "platinum";
                } else if (score > 70) {
                    return "gold";
                } else if (score > 50) {
                    return "silver";
                } else {
                    return "bronze";
                }
            } else {
                if (score > 90) {
                    return "high";
                } else if (score > 50) {
                    return "medium";
                } else {
                    return "low";
                }
            }
        } else {
            return "inactive";
        }
    }
}
