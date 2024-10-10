package com.hxngxd.entities;

import java.time.LocalDate;

/**
 * Lớp Person là lớp cha của User và Author, đại diện cho một cá nhân trong hệ thống.
 */
public class Person {

    protected int id;
    protected String firstName;
    protected String lastName;
    protected LocalDate dateOfBirth;
//    protected Img photo;

    public Person() {
    }

    public Person(int id, String firstName, String lastName, LocalDate dateOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

}