package com.hxngxd.entities;

import com.hxngxd.database.DatabaseManager;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.utils.ImageHandler;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Author extends Person {

    private String biography;

    private LocalDate dayOfDeath;

    private final List<Book> books = new ArrayList<>();

    public static final HashMap<Integer, Author> authorMap = new HashMap<>();

    public static void initialize()
            throws DatabaseException {
        authorMap.clear();

        String query = "select * from author";
        DatabaseManager.getInstance().select("getting authors", query, resultSet -> {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                if (authorMap.containsKey(id)) {
                    continue;
                }
                Author author = new Author(id);
                author.setFirstName(resultSet.getString("firstName"));
                author.setLastName(resultSet.getString("lastName"));
                author.setDayOfDeath(resultSet.getDate("dateOfBirth").toLocalDate());
                byte[] photo = resultSet.getBytes("photo");
                if (photo != null) {
                    author.setImage(ImageHandler.byteArrayToImage(photo));
                }
                author.setBiography(resultSet.getString("biography"));
                Date dod = resultSet.getDate("dayOfDeath");
                if (dod != null) {
                    author.setDayOfDeath(dod.toLocalDate());
                }
                authorMap.put(id, author);
            }
            return null;
        });
    }

    public Author() {
    }

    public Author(int id) {
        this.id = id;
    }

    public Author(int id, String firstName, String lastName, LocalDate dateOfBirth,
                  String biography, LocalDate dayOfDeath) {
        super(id, firstName, lastName, dateOfBirth);
        this.biography = biography;
        this.dayOfDeath = dayOfDeath;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public LocalDate getDayOfDeath() {
        return dayOfDeath;
    }

    public void setDayOfDeath(LocalDate dayOfDeath) {
        this.dayOfDeath = dayOfDeath;
    }

    public void addBook(Book book) {
        if (!this.books.contains(book)) {
            this.books.add(book);
        }
    }

    public List<Book> getBooks() {
        return books;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Author)) {
            return false;
        }
        return this.id == ((Author) other).getId();
    }

}