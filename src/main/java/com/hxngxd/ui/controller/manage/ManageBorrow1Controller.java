package com.hxngxd.ui.controller.manage;

import com.hxngxd.actions.Borrowing;
import com.hxngxd.enums.BorrowStatus;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.service.BorrowService;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.utils.Formatter;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ManageBorrow1Controller extends ActionManageController<Borrowing> {

    @FXML
    private TableColumn<Borrowing, String> bookColumn;

    @FXML
    private TableColumn<Borrowing, String> handlerColumn;

    @FXML
    private TableColumn<Borrowing, String> estimatedReturnDateColumn;

    @FXML
    private TableColumn<Borrowing, String> statusColumn;

    @FXML
    private TableColumn<Borrowing, String> approvalDateColumn;

    @FXML
    private TableColumn<Borrowing, String> borrowDateColumn;

    @FXML
    private TableColumn<Borrowing, String> actualReturnDateColumn;

    private final BorrowService borrowService = BorrowService.getInstance();

    @Override
    @FXML
    protected void initialize() {
        super.initialize();

        bookColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Borrowing, String> param) {
                return new ReadOnlyObjectWrapper<>(
                        String.format("(%d) %s", param.getValue().getBook().getId(), param.getValue().getBook().getTitle())
                );
            }
        });

        handlerColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Borrowing, String> param) {
                return new ReadOnlyObjectWrapper<>(
                        String.format("(%d) %s", param.getValue().getHandler().getId(), param.getValue().getHandler().getFullNameLastThenFirst())
                );
            }
        });

        estimatedReturnDateColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Borrowing, String> param) {
                LocalDate estimatedReturnDate = param.getValue().getEstimatedReturnDate();
                return new ReadOnlyObjectWrapper<>(
                        estimatedReturnDate != null ? Formatter.formatDateSlash(estimatedReturnDate) : null
                );
            }
        });

        statusColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Borrowing, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getStatus().msg());
            }
        });

        approvalDateColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Borrowing, String> param) {
                LocalDateTime approvalDate = param.getValue().getApprovalDate();
                return new ReadOnlyObjectWrapper<>(
                        approvalDate != null ? Formatter.formatDateTime(approvalDate) : "Chưa xử lý"
                );
            }
        });

        borrowDateColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Borrowing, String> param) {
                LocalDateTime borrowDate = param.getValue().getBorrowDate();
                return new ReadOnlyObjectWrapper<>(
                        borrowDate != null ? Formatter.formatDateTime(borrowDate) : "Chưa nhận sách"
                );
            }
        });

        actualReturnDateColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Borrowing, String> param) {
                LocalDateTime actualReturnDate = param.getValue().getActualReturnDate();
                return new ReadOnlyObjectWrapper<>(
                        actualReturnDate != null ? Formatter.formatDateTime(actualReturnDate) : "Chưa trả sách"
                );
            }
        });
    }

    @Override
    protected void filterItems() {

    }

    @Override
    protected void addSearchFields() {

    }

    @Override
    public void onUpdate() {
        try {
            borrowService.loadAll();
            itemList.clear();
            itemList = FXCollections.observableArrayList(Borrowing.borrowingSet);
            super.onUpdate();
        } catch (DatabaseException e) {
            PopupManager.info(LogMsg.GENERAL_FAIL.msg("update borrowing list"));
        }
    }

    @FXML
    private void approve() {
        unPending("Approve this request?", BorrowStatus.APPROVED);
    }

    @FXML
    private void reject() {
        unPending("Reject this request?", BorrowStatus.REJECTED);
    }

    @FXML
    private void lend() {
        if (getSelected() == null) {
            return;
        }
        if (getSelected().getStatus() != BorrowStatus.APPROVED) {
            PopupManager.info("Not right now");
            return;
        }
        updateStatus("Lend this book?", BorrowStatus.BORROWED);
    }

    @FXML
    private void returnBook() {
        if (getSelected() == null) {
            return;
        }
        if (getSelected().getStatus() != BorrowStatus.BORROWED) {
            PopupManager.info("Not right now");
            return;
        }
        boolean isLate = LocalDate.now().isAfter(getSelected().getEstimatedReturnDate());
        updateStatus("Book is returned?", isLate ? BorrowStatus.RETURNED_LATE : BorrowStatus.RETURNED_ON_TIME);
    }

    private void unPending(String msg, BorrowStatus status) {
        if (getSelected() == null) {
            return;
        }
        if (getSelected().getStatus() != BorrowStatus.PENDING) {
            PopupManager.info("Already handled");
            return;
        }
        updateStatus(msg, status);
    }

    private void updateStatus(String msg, BorrowStatus status) {
        PopupManager.confirm(msg, () -> {
            try {
                borrowService.updateStatus(getSelected(), status);
                onUpdate();
            } catch (DatabaseException e) {
                PopupManager.info(e.getMessage());
            } finally {
                PopupManager.closePopup();
            }
        });
    }

}