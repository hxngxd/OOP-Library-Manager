package com.hxngxd.ui;

import com.hxngxd.entities.Author;
import com.hxngxd.entities.Book;
import com.hxngxd.entities.Genre;
import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.enums.UI;
import com.hxngxd.service.BookService;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.controller.book.BookCardController;
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

public final class BookGalleryController {

    private static final Logger log = LogManager.getLogger(BookGalleryController.class);

    private static final List<FXMLLoader> bookCards = new ArrayList<>();

    @FXML
    private FlowPane bookCardContainer;

    @FXML
    private TextField searchField;

    public static boolean isShowingSavedBook = false;

    private User currentUser = null;

    @FXML
    private void initialize() {
        searchBook();
        onActive();
    }

    public void onActive() {
        currentUser = UserService.getInstance().getCurrentUser();
        loadBookCards();
        showBookCards();
    }

    private void loadBookCards() {
        bookCards.clear();

        List<Book> bookList = new ArrayList<>(BookService.bookMap.values());
        Collections.shuffle(bookList);
        for (Book book : bookList) {
            try {
                FXMLLoader loader = UIManager.load(UI.BOOK_CARD);
                bookCards.add(loader);
                ((BookCardController) loader.getController()).setBook(book);
            } catch (NullPointerException e) {
                e.printStackTrace();
                log.error(LogMsg.General.FAIL.getMSG("create book card"), e.getMessage());
            }
        }
    }

    private void showBookCards(String info) {
        bookCardContainer.getChildren().clear();
        if (info == null || info.isEmpty()) {
            for (FXMLLoader bookCard : bookCards) {
                BookCardController bookCardController = bookCard.getController();
                Book book = bookCardController.getBook();
                Parent root = bookCard.getRoot();
                if (isShowingSavedBook) {
                    if (!currentUser.getSavedBooks().contains(book)) {
                        bookCardContainer.getChildren().remove(root);
                        continue;
                    }
                }
                if (!bookCardContainer.getChildren().contains(root)) {
                    bookCardContainer.getChildren().add(root);
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
                approxMatch = approxMatch || (InputHandler.isUnidecodeSimilar(bookInfo, info));
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

    public void showBookCards() {
        showBookCards(null);
    }

    public void showBookCardsBySearch() {
        showBookCards(searchField.getText());
    }

    public void searchBook() {
        PauseTransition pause = new PauseTransition(Duration.millis(250));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 63) {
                searchField.setText(newValue.substring(0, 63));
            } else {
                pause.setOnFinished(event -> showBookCardsBySearch());
                pause.playFromStart();
            }
        });
    }

    public void setIsShowingSavedBook(boolean isShowingSavedBook) {
        BookGalleryController.isShowingSavedBook = isShowingSavedBook;
    }

    public static BookGalleryController getInstance() {
        return UIManager.getControllerOnce(UI.BOOK_GALLERY);
    }

}