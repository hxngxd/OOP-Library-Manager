package com.hxngxd.actions;

import com.hxngxd.entities.Book;
import com.hxngxd.enums.BorrowStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Borrowing extends Action {
    private int bookId;
    private int handlerId;
    private final LocalDateTime requestDate;
    private LocalDateTime approvalDate;
    private LocalDateTime borrowDate;
    private LocalDate estimatedReturnDate;
    private LocalDate actualReturnDate;
    private BorrowStatus status;

    public Borrowing(int id, int userId, LocalDateTime timestamp, int bookId,
                     int handlerId, LocalDateTime requestDate, LocalDate estimatedReturnDate) {
        super(id, userId, timestamp);
        this.bookId = bookId;
        this.handlerId = handlerId;
        this.requestDate = requestDate;
        this.approvalDate = null;
        this.borrowDate = null;
        this.estimatedReturnDate = estimatedReturnDate;
        this.actualReturnDate = null;
        this.status = BorrowStatus.PENDING;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(int handlerId) {
        this.handlerId = handlerId;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getEstimatedReturnDate() {
        return estimatedReturnDate;
    }

    public void setEstimatedReturnDate(LocalDate estimatedReturnDate) {
        this.estimatedReturnDate = estimatedReturnDate;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public BorrowStatus getStatus() {
        return status;
    }

    public void setStatus(BorrowStatus status) {
        this.status = status;
    }
}