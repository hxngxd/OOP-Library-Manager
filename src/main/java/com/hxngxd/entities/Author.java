package com.hxngxd.entities;

import com.hxngxd.database.DatabaseManager;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.utils.ImageHandler;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public final class Author extends Person {

    private String biography;
    private LocalDate dayOfDeath;

    private final Set<Book> books = new HashSet<>();

    public static final Set<Author> authorSet = new HashSet<>();
    public static final HashMap<Integer, Author> authorMap = new HashMap<>();

    public static void loadAll()
            throws DatabaseException {
        authorSet.clear();
        authorMap.clear();

        String query = "select * from author";
        DatabaseManager.getInstance().select("getting authors", query, resultSet -> {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");

                Author author = new Author(id);

                author.setFirstName(resultSet.getString("firstName"));

                author.setLastName(resultSet.getString("lastName"));

                Date dob = resultSet.getDate("dateOfBirth");
                if (dob != null) {
                    author.setDayOfDeath(dob.toLocalDate());
                }

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
        authorSet.addAll(authorMap.values());
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
        books.add(book);
    }

}