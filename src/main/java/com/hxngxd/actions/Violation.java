package com.hxngxd.actions;

import java.time.LocalDateTime;
import java.util.List;

public class Violation extends Action {
    private String description;

    public Violation(int id, int userId, LocalDateTime timestamp, String description) {
        super(id, userId, timestamp);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static List<Violation> getUsersViolations(int userId) {
        return null;
    }
}