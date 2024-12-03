package com.hxngxd.ui.controller;

import com.hxngxd.entities.Author;
import com.hxngxd.entities.Book;
import com.hxngxd.entities.Genre;
import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.enums.UI;
import com.hxngxd.ui.UIManager;
import com.hxngxd.ui.controller.book.BookCardController;
import com.hxngxd.utils.InputHandler;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class BookGalleryController extends NavigateController {

    private final List<FXMLLoader> bookCards = new ArrayList<>();

    @FXML
    private FlowPane bookCardContainer;

    @FXML
    private TextField searchField;

    private boolean showSaved = false;

    @FXML
    private void initialize() {
        searchBook();
        onActive();
    }

    @Override
    public void onActive() {
        searchField.clear();
        loadBookCards();
        showBookCards(null);
    }

    private void loadBookCards() {
        bookCards.clear();

        for (Book book : Book.bookSet) {
            try {
                FXMLLoader loader = UIManager.load(UI.BOOK_CARD);
                bookCards.add(loader);
                ((BookCardController) loader.getController()).setBook(book);
            } catch (NullPointerException e) {
                log.error(LogMsg.GENERAL_FAIL.msg("load book card"), e);
            }
        }
    }

    private void showBookCards(String search) {
        bookCardContainer.getChildren().clear();

        User user = User.getCurrent();
        for (FXMLLoader bookCard : bookCards) {
            BookCardController bookCardController = bookCard.getController();
            Book book = bookCardController.getBook();
            if (showSaved && !user.getSavedBooks().contains(book)) {
                continue;
            }

            boolean match = false;
            if (search == null || search.isEmpty()) {
                match = true;
            } else {
                Set<String> infos = new HashSet<>();
                infos.add(book.getTitle());
                infos.add(book.getShortDescription());
                for (Author author : book.getAuthors()) {
                    infos.add(author.getFullNameFirstThenLast());
                }
                for (Genre genre : book.getGenres()) {
                    infos.add(genre.getName());
                }
                String combinedInfos = String.join(" ", infos);
                match = InputHandler.isUnidecodeSimilar(combinedInfos, search);
            }

            if (match) {
                bookCardContainer.getChildren().add(bookCard.getRoot());
            }
        }
    }

    public void showBookCards() {
        showBookCards(null);
    }

    public void searchBook() {
        PauseTransition pause = new PauseTransition(Duration.millis(250));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 63) {
                searchField.setText(newValue.substring(0, 63));
            } else {
                pause.setOnFinished(event -> {
                    showBookCards(searchField.getText());
                });
                pause.playFromStart();
            }
        });
    }

    public boolean isShowSaved() {
        return showSaved;
    }

    public void setShowSaved(boolean showSaved) {
        this.showSaved = showSaved;
    }

}