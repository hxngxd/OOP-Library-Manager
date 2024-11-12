package com.hxngxd.entities;

import com.hxngxd.database.DatabaseManager;
import com.hxngxd.exceptions.DatabaseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Genre extends Entity {

    private String name;

    private String description;

    private final List<Book> books = new ArrayList<>();

    public static final HashMap<Integer, Genre> genreMap = new HashMap<>();

    public static void initialize()
            throws DatabaseException {
        genreMap.clear();

        String query = "select * from genre";
        DatabaseManager.getInstance().select("getting genres", query, resultSet -> {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                if (genreMap.containsKey(id)) {
                    continue;
                }
                Genre genre = new Genre(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description")
                );
                genreMap.put(id, genre);
            }
            return null;
        });
    }

    public Genre() {
    }

    public Genre(int id) {
        super(id);
    }

    public Genre(int id, String name, String description) {
        super(id);
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1);
        this.description = description;
    }

    public void addBook(Book book) {
        if (!this.books.contains(book)) {
            this.books.add(book);
        }
    }

    public List<Book> getBooks() {
        return books;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Genre)) {
            return false;
        }
        return this.id == ((Genre) other).getId();
    }
}