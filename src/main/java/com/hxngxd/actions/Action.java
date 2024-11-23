package com.hxngxd.actions;

import com.hxngxd.entities.User;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return id == action.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
    
}