package com.hxngxd.ui.controller;

import com.hxngxd.entities.Author;
import com.hxngxd.entities.Book;
import com.hxngxd.entities.Genre;
import com.hxngxd.enums.LogMessages;
import com.hxngxd.enums.UI;
import com.hxngxd.service.BookService;
import com.hxngxd.ui.UIManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookGalleryController {
    private static final Logger log = LogManager.getLogger(BookGalleryController.class);
    @FXML
    private FlowPane bookCardContainer;

    @FXML
    private void initialize() {
        Author.initialize();
        Genre.initialize();
        BookService.initialize();
        showBooks();
    }

    private void showBooks() {
        List<Book> bookList = new ArrayList<>(Book.bookMap.values());
        Collections.shuffle(bookList);

        for (Book book : bookList) {
            try {
                FXMLLoader loader = UIManager.load(UI.BOOK_CARD);
                VBox bookCard = (VBox) loader.getRoot();
                ((BookCardController) loader.getController()).setBook(book);
                bookCardContainer.getChildren().add(bookCard);
            } catch (NullPointerException e) {
                e.printStackTrace();
                log.error(LogMessages.General.FAIL.getMessage("create book card"),
                        e.getMessage());
            }
        }
    }
}