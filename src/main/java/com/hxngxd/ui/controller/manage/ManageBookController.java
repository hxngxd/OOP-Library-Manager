package com.hxngxd.ui.controller.manage;

import com.hxngxd.entities.Book;
import com.hxngxd.enums.UI;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.exceptions.UserException;
import com.hxngxd.service.BookService;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.ui.UIManager;
import com.hxngxd.ui.controller.BorrowingRequestController;
import com.hxngxd.ui.controller.MainController;
import com.hxngxd.ui.controller.book.BookEditController;
import com.hxngxd.ui.controller.book.BookPreviewController;
import com.hxngxd.utils.InputHandler;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.util.Callback;

public final class ManageBookController extends EntityManageController<Book> {

    @FXML
    private TableColumn<Book, String> bookNameColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, Integer> yearOfPublicationColumn;

    @FXML
    private TableColumn<Book, String> genreColumn;

    @FXML
    private TableColumn<Book, Integer> totalCopiesColumn;

    @FXML
    private TableColumn<Book, String> shortDescriptionColumn;

    @FXML
    private TableColumn<Book, Integer> availableCopiesColumn;

    private final BookService bookService = BookService.getInstance();

    @FXML
    protected void initialize() {
        super.initialize();

        bookNameColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getTitle());
            }
        });

        authorColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().authorsToString());
            }
        });

        yearOfPublicationColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Book, Integer> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getYearOfPublication());
            }
        });

        genreColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().genresToString());
            }
        });

        totalCopiesColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Book, Integer> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getTotalCopies());
            }
        });

        shortDescriptionColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getShortDescription());
            }
        });

        availableCopiesColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Book, Integer> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getAvailableCopies());
            }
        });
    }

    @Override
    protected void filterItems() {
        String selectedField = searchFieldComboBox.getValue();
        String searchText = InputHandler.unidecode(searchField.getText());

        FilteredList<Book> filteredData = new FilteredList<>(itemList, book -> {
            if (searchText.isEmpty()) {
                return true;
            }
            String author = book.authorsToString();
            String genre = book.genresToString();
            return switch (selectedField) {
                case "ID" -> String.valueOf(book.getId()).contains(searchText);

                case "Tên sách" -> book.getTitle() != null
                        && InputHandler.isUnidecodeSimilar(book.getTitle(), searchText);

                case "Tác giả" -> book.getAuthors() != null
                        && InputHandler.isUnidecodeSimilar(author, searchText);

                case "Thể loại" -> book.getGenres() != null
                        && InputHandler.isUnidecodeSimilar(genre, searchText);

                case "Mô tả" -> book.getShortDescription() != null
                        && InputHandler.isUnidecodeSimilar(book.getShortDescription(), searchText);

                default -> false;
            };
        });
        itemTableView.setItems(filteredData);
    }

    @Override
    protected void addSearchFields() {
        searchFieldComboBox.setItems(FXCollections.observableArrayList(
                idColumn.getText(),
                bookNameColumn.getText(),
                authorColumn.getText(),
                genreColumn.getText(),
                shortDescriptionColumn.getText()
        ));
        searchFieldComboBox.setValue(idColumn.getText());
    }

    @Override
    @FXML
    public void onUpdate() {
        try {
            bookService.loadAll();
            itemList.clear();
            itemList = FXCollections.observableArrayList(Book.bookSet);
            super.onUpdate();
        } catch (DatabaseException e) {
            PopupManager.info("Cập nhật danh sách thất bại");
        }
    }

    @FXML
    private void addBook() {
        UI ui = UI.BOOK_EDIT;
        MainController mainController = UIManager.getController(UI.MAIN);
        if (mainController.getCurrentTab() == ui) {
            return;
        }
        mainController.setCurrentTab(ui);
        mainController.navigate(UIManager.getRootOnce(ui));
        ((BookEditController) UIManager.getController(ui)).setBook(null);
        UIManager.getActivableController(ui).onActive();
    }

    @FXML
    private void editBook() {
        if (getSelected() == null) {
            return;
        }
        UI ui = UI.BOOK_EDIT;
        MainController mainController = UIManager.getController(UI.MAIN);
        if (mainController.getCurrentTab() == ui) {
            return;
        }
        mainController.setCurrentTab(ui);
        mainController.navigate(UIManager.getRootOnce(ui));
        ((BookEditController) UIManager.getController(ui)).setBook(getSelected());
        UIManager.getActivableController(ui).onActive();
    }

    @FXML
    private void removeBook() {
        if (getSelected() == null) {
            return;
        }
        String message = "You sure about removing this book (can't be undone)?";
        PopupManager.confirm(message, () -> {
            try {
                bookService.deleteBook(getSelectedId());
                onUpdate();
            } catch (DatabaseException | UserException e) {
                PopupManager.info(e.getMessage());
            } finally {
                PopupManager.closePopup();
            }
        });
    }

    @FXML
    private void seeBook() {
        if (getSelected() == null) {
            return;
        }
        BookPreviewController bookPreviewController = UIManager.getController(UI.BOOK_PREVIEW);
        bookPreviewController.setBook(Book.bookMap.get(getSelectedId()));
        bookPreviewController.showDetail();
    }

}
