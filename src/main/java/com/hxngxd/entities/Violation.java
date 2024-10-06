package com.hxngxd.entities;

import java.time.LocalDateTime;

/**
 * Lớp Violation đại diện cho một vi phạm của người dùng.
 */
public class Violation {

    private int id;
    private int user;
    private LocalDateTime violationDate;
    private String description;

}
