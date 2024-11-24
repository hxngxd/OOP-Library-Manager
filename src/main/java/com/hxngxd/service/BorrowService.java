package com.hxngxd.service;

import com.hxngxd.actions.Borrowing;
import com.hxngxd.entities.Book;
import com.hxngxd.entities.User;
import com.hxngxd.enums.BorrowStatus;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.enums.Role;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.exceptions.ValidationException;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class BorrowService extends Service {

    private BorrowService() {
    }

    private static class SingletonHolder {
        private static final BorrowService instance = new BorrowService();
    }

    public static BorrowService getInstance() {
        return BorrowService.SingletonHolder.instance;
    }

    @Override
    public void loadAll() throws DatabaseException {
        Borrowing.borrowingSet.clear();
        boolean isUser = User.getCurrent().getRole() == Role.USER;
        boolean isAdmin = User.getCurrent().getRole() == Role.ADMIN;
        String query = String.format("select * from borrowing where %s = ?", isUser ? "requesterId" : "handlerId");
        query = (isAdmin ? "select * from borrowing" : query);
        db.select("get borrowing", query, resultSet -> {
            while (resultSet.next()) {
                Borrowing borrowing = new Borrowing(resultSet.getInt("id"));

                borrowing.setUser(User.userMap.get(resultSet.getInt("requesterId")));

                borrowing.setHandler(User.userMap.get(resultSet.getInt("handlerId")));

                borrowing.setBook(Book.bookMap.get(resultSet.getInt("bookId")));

                borrowing.setTimestamp(resultSet.getTimestamp("requestDate").toLocalDateTime());

                Timestamp approvalDate = resultSet.getTimestamp("approvalDate");
                if (approvalDate != null) {
                    borrowing.setApprovalDate(approvalDate.toLocalDateTime());
                }

                Timestamp borrowDate = resultSet.getTimestamp("borrowDate");
                if (borrowDate != null) {
                    borrowing.setBorrowDate(borrowDate.toLocalDateTime());
                }

                borrowing.setEstimatedReturnDate(resultSet.getDate("estimatedReturnDate").toLocalDate());

                Timestamp actualReturnDate = resultSet.getTimestamp("actualReturnDate");
                if (actualReturnDate != null) {
                    borrowing.setActualReturnDate(actualReturnDate.toLocalDateTime());
                }

                borrowing.setStatus(BorrowStatus.valueOf(resultSet.getString("status")));

                Borrowing.borrowingSet.add(borrowing);
            }
            return null;
        }, isAdmin ? null : User.getCurrent().getId());
    }

    public void submitRequest(User requester, Book book, LocalDate estimatedReturnDate)
            throws DatabaseException, ValidationException {
        int handlerId = -1;
        List<Integer> moderatorList = new ArrayList<>();
        for (User user : User.userSet) {
            if (user.getRole() == Role.MODERATOR) {
                moderatorList.add(user.getId());
            }
        }
        handlerId = moderatorList.get(ThreadLocalRandom.current().nextInt(moderatorList.size()));

        if (estimatedReturnDate.isBefore(LocalDate.now()) ||
                estimatedReturnDate.isEqual(LocalDate.now())) {
            throw new ValidationException("Return date must be after today");
        }

        if (ChronoUnit.DAYS.between(LocalDate.now(), estimatedReturnDate) > 30) {
            throw new ValidationException("Less than 30 days please");
        }

        db.insert("borrowing", false,
                List.of("requesterId", "bookId", "handlerId", "estimatedReturnDate"),
                requester.getId(), book.getId(), handlerId, Date.valueOf(estimatedReturnDate));

        loadAll();

        log.info(LogMsg.GENERAL_SUCCESS.msg("add borrowing"));
    }

    public void updateStatus(Borrowing borrowing, BorrowStatus status)
            throws DatabaseException {
        if (status == BorrowStatus.PENDING) {
            return;
        }
        if (status == BorrowStatus.APPROVED
                || status == BorrowStatus.REJECTED) {
            db.update("borrowing",
                    List.of("approvalDate", "status"),
                    List.of(Timestamp.valueOf(LocalDateTime.now()), status.name()),
                    List.of("id"), List.of(borrowing.getId()));
        } else if (status == BorrowStatus.BORROWED) {
            db.update("borrowing",
                    List.of("borrowDate", "status"),
                    List.of(Timestamp.valueOf(LocalDateTime.now()), status.name()),
                    List.of("id"), List.of(borrowing.getId()));
            BookService.getInstance().loadAll();
            db.update("book", "availableCopies", borrowing.getBook().getAvailableCopies() - 1,
                    "id", borrowing.getBook().getId());
        } else if (status == BorrowStatus.RETURNED_LATE
                || status == BorrowStatus.RETURNED_ON_TIME) {
            db.update("borrowing",
                    List.of("actualReturnDate", "status"),
                    List.of(Timestamp.valueOf(LocalDateTime.now()), status.name()),
                    List.of("id"), List.of(borrowing.getId()));
            BookService.getInstance().loadAll();
            db.update("book", "availableCopies", borrowing.getBook().getAvailableCopies() + 1,
                    "id", borrowing.getBook().getId());
        }
        loadAll();
        log.info(LogMsg.GENERAL_SUCCESS.msg("update borrow status"));
    }

}