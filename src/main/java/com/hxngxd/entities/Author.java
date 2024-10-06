package com.hxngxd.entities;

import java.time.LocalDate;
import java.util.List;

public class Author {
    private String biography;
    private LocalDate dayOfDeath;

    public boolean addBook(int bookId){
        return false;
    }

    public boolean removeBook(int bookId){
        return false;
    }

    public List<Book> getBooks(){
        return null;
    }
}
