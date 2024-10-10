package com.hxngxd.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Lớp Person là lớp cha của User và Author, đại diện cho một cá nhân trong hệ thống.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    protected int id;
    protected String firstName;
    protected String lastName;
    protected LocalDate dateOfBirth;
//    protected Img photo;
}