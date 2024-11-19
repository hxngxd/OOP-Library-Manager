package com.hxngxd.actions;

import com.hxngxd.entities.User;

import java.time.LocalDateTime;

public abstract class Action {

    protected int id;

    protected User user;

    protected LocalDateTime timestamp;

    public Action() {
    }

    public Action(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}