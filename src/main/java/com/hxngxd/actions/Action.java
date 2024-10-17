package com.hxngxd.actions;

import java.time.LocalDateTime;

abstract class Action {
    protected int id;
    protected int userId;
    protected LocalDateTime timestamp;

    public Action(int id, int userId, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
