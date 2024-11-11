package com.hxngxd.ui.controller;

import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMessages;
import com.hxngxd.enums.UI;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.UIManager;
import com.hxngxd.utils.ImageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class MainController extends NavigateController {
    private static final Logger log = LogManager.getLogger(MainController.class);
    private UI currentTab;
    @FXML
    private ImageView image;
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label userInfoLabel;
    @FXML
    private SplitPane root;

    @FXML
    private void initialize() {
        User user = UserService.getInstance().getCurrentUser();
        if (user.getImage() != null) {
            setImage(
                    ImageHandler.cropImageByRatio(user.getImage(), 1, 1)
            );
        }
        setFullNameLabel(user.getFullNameFirstThenLast());
        setUserInfoLabel(user.toString());

        currentTab = UI.BOOK_GALLERY;
        try {
            AnchorPane bookGallery = (AnchorPane) Objects.requireNonNull(
                    UIManager.loadOnce(UI.BOOK_GALLERY)).getRoot();
            root.getItems().add(bookGallery);
        } catch (NullPointerException e) {
            e.printStackTrace();
            log.error(LogMessages.General.FAIL.getMessage("load book gallery"),
                    e.getMessage());
        }
    }

    public void setImage(Image image) {
        this.image.setFitHeight(90);
        this.image.setFitWidth(90);
        this.image.setPreserveRatio(true);
        Circle clip = new Circle(45, 45, 45);
        this.image.setClip(clip);
        this.image.setImage(image);
    }

    public void setFullNameLabel(String fullname) {
        this.fullNameLabel.setText(fullname);
    }

    public void setUserInfoLabel(String info) {
        this.userInfoLabel.setText(info);
    }

    @FXML
    private void showAccount(ActionEvent event) {
        if (currentTab == UI.ACCOUNT) {
            return;
        }
        currentTab = UI.ACCOUNT;
        navigate(UIManager.loadOnce(UI.ACCOUNT).getRoot());
    }

    @FXML
    private void showHome(ActionEvent event) {
        BookGalleryController bookGalleryController = (BookGalleryController)
                UIManager.Loaders.get(UI.BOOK_GALLERY).getController();
        bookGalleryController.setIsShowingSavedBook(false);
        bookGalleryController.showBookCards(null);
        showBookGallery();
    }

    @FXML
    private void showSavedBook(ActionEvent event) {
        BookGalleryController bookGalleryController = (BookGalleryController)
                UIManager.Loaders.get(UI.BOOK_GALLERY).getController();
        bookGalleryController.setIsShowingSavedBook(true);
        bookGalleryController.showBookCards(null);
        showBookGallery();
    }

    private void showBookGallery() {
        if (currentTab == UI.BOOK_GALLERY) {
            return;
        }
        currentTab = UI.BOOK_GALLERY;
        navigate(UIManager.loadOnce(UI.BOOK_GALLERY).getRoot());
        FXMLLoader loader = UIManager.loadOnce(UI.BOOK_PREVIEW);
        BookPreviewController bpController = loader.getController();
        if (bpController.isPreviewing()) {
            root.getItems().add(loader.getRoot());
        }
    }

    private void navigate(Parent tab) {
        if (root.getItems().contains(tab)) {
            return;
        }
        AnchorPane navigation = (AnchorPane) root.getItems().getFirst();
        root.getItems().clear();
        root.getItems().addAll(navigation, tab);
    }
}
