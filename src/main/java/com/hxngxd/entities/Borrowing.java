package com.hxngxd.entities;

import com.hxngxd.enums.BorrowStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Borrowing {
    private int id;
    private int requester;
    private int book;
    private int handler;
    private LocalDateTime requestDate;
    private LocalDateTime approvalDate;
    private LocalDateTime borrowDate;
    private LocalDate estimatedReturnDate;
    private LocalDate actualReturnDate;
    private BorrowStatus status;
}
