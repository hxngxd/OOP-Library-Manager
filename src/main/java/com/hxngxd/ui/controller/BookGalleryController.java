package com.hxngxd.ui.controller;

import com.hxngxd.entities.Author;
import com.hxngxd.entities.Book;
import com.hxngxd.entities.Genre;
import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMessages;
import com.hxngxd.enums.UI;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.UIManager;
import com.hxngxd.utils.InputHandler;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookGalleryController {
    private static final Logger log = LogManager.getLogger(BookGalleryController.class);
    private static final List<FXMLLoader> bookCards = new ArrayList<>();
    @FXML
    private FlowPane bookCardContainer;
    @FXML
    private TextField searchField;
    public static boolean isShowingSavedBook = false;
    private final User currentUser = UserService.getInstance().getCurrentUser();

    @FXML
    private void initialize() {
        loadBookCards();
        showBookCards(null);
        searchBook();
    }

    private void loadBookCards() {
        List<Book> bookList = new ArrayList<>(Book.bookMap.values());
        Collections.shuffle(bookList);

        for (Book book : bookList) {
            try {
                FXMLLoader loader = UIManager.load(UI.BOOK_CARD);
                bookCards.add(loader);
                ((BookCardController) loader.getController()).setBook(book);
            } catch (NullPointerException e) {
                e.printStackTrace();
                log.error(LogMessages.General.FAIL.getMessage("create book card"),
                        e.getMessage());
            }
        }
    }

    public void showBookCards(String info) {
        if (info == null || info.isEmpty()) {
            for (FXMLLoader bookCard : bookCards) {
                BookCardController bookCardController = bookCard.getController();
                Book book = bookCardController.getBook();
                if (isShowingSavedBook) {
                    if (!currentUser.getSavedBooks().contains(book)) {
                        bookCardContainer.getChildren().remove(bookCard.getRoot());
                        continue;
                    }
                }
                if (!bookCardContainer.getChildren().contains(bookCard.getRoot())) {
                    bookCardContainer.getChildren().add(bookCard.getRoot());
                }
            }
            return;
        }

        for (FXMLLoader bookCard : bookCards) {
            BookCardController bookCardController = bookCard.getController();
            Book book = bookCardController.getBook();
            if (isShowingSavedBook) {
                if (!currentUser.getSavedBooks().contains(book)) {
                    bookCardContainer.getChildren().remove(bookCard.getRoot());
                    continue;
                }
            }
            Parent card = bookCard.getRoot();
            boolean approxMatch = false;
            List<String> bookInfos = new ArrayList<>();
            bookInfos.add(book.getTitle());
            bookInfos.add(book.getShortDescription());
            for (Author author : book.getAuthors()) {
                bookInfos.add(author.getFullNameFirstThenLast());
            }
            for (Genre genre : book.getGenres()) {
                bookInfos.add(genre.getName());
            }
            for (String bookInfo : bookInfos) {
                approxMatch = approxMatch || (InputHandler.minEditDistance(bookInfo, info)
                        <= InputHandler.editDistanceThreshHold);
            }
            if (approxMatch) {
                if (!bookCardContainer.getChildren().contains(card)) {
                    bookCardContainer.getChildren().add(bookCard.getRoot());
                }
            } else {
                bookCardContainer.getChildren().remove(card);
            }
        }
    }

    public void searchBook() {
        PauseTransition pause = new PauseTransition(Duration.millis(200));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 63) {
                searchField.setText(newValue.substring(0, 63));
            } else {
                pause.setOnFinished(event -> showBookCards(searchField.getText()));
                pause.playFromStart();
            }
        });
    }

    public void setIsShowingSavedBook(boolean isShowingSavedBook) {
        BookGalleryController.isShowingSavedBook = isShowingSavedBook;
    }
}