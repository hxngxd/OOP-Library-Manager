package com.hxngxd.actions;

import com.hxngxd.entities.Book;
import com.hxngxd.entities.User;
import com.hxngxd.enums.BorrowStatus;
import com.hxngxd.utils.Formatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public final class Borrowing extends Action {

    private Book book;
    private User handler;

    private LocalDateTime approvalDate;
    private LocalDateTime borrowDate;
    private LocalDate estimatedReturnDate;
    private LocalDateTime actualReturnDate;

    private BorrowStatus status;

    public static final Set<Borrowing> borrowingSet = new HashSet<>();

    public Borrowing(int id) {
        super(id);
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getHandler() {
        return handler;
    }

    public void setHandler(User handler) {
        this.handler = handler;
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

    public LocalDateTime getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDateTime actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public BorrowStatus getStatus() {
        return status;
    }

    public void setStatus(BorrowStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("• Ngày yêu cầu: %s\n• Ngày nhận sách: %s\n• Ngày trả dự kiến: %s\n• Ngày trả thực tế: %s",
                Formatter.formatDateTime(timestamp),
                borrowDate == null ? "Chưa nhận sách" : Formatter.formatDateTime(borrowDate),
                Formatter.formatDateDash(estimatedReturnDate),
                actualReturnDate == null ? "Chưa trả sách" : Formatter.formatDateTime(actualReturnDate));
    }
}