package com.hxngxd.entities;

import java.time.LocalDate;

public abstract class Person extends EntityWithPhoto {

    protected String firstName;

    protected String lastName;

    protected LocalDate dateOfBirth;

    public Person() {
    }

    public Person(int id) {
        super(id);
    }

    public Person(int id, String firstName, String lastName, LocalDate dateOfBirth) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
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

    public String getFullNameFirstThenLast() {
        return this.firstName + " " + this.lastName;
    }

    public String getFullNameLastThenFirst() {
        return this.lastName + " " + this.firstName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
}