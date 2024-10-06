package com.hxngxd.entities;

import com.hxngxd.entities.Book;
import com.hxngxd.enums.BorrowStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Lớp Borrowing đại diện cho giao dịch mượn sách.
 */
public class Borrowing {

    private int id;
    private int requester;
    private Book book;
    private int handler;
    private LocalDateTime requestDate;
    private LocalDateTime approvalDate;
    private LocalDateTime borrowDate;
    private LocalDate estimatedReturnDate;
    private LocalDate actualReturnDate;
    private BorrowStatus status;

}