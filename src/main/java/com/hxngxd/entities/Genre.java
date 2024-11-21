package com.hxngxd.entities;

import com.hxngxd.database.DatabaseManager;
import com.hxngxd.exceptions.DatabaseException;

import java.util.*;

public final class Genre extends Entity {

    private String name;
    private String description;

    private final Set<Book> books = new HashSet<>();

    public static final HashMap<Integer, Genre> genreMap = new HashMap<>();

    public static void loadAll()
            throws DatabaseException {
        genreMap.clear();
        String query = "select * from genre";
        DatabaseManager.getInstance().select("getting genres", query, resultSet -> {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Genre genre = new Genre(
                        id,
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
        this.books.add(book);
    }

    public String getName() {
        return name;
    }

}