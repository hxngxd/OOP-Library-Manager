package com.hxngxd.ui.controller;

import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMessages;
import com.hxngxd.enums.Role;
import com.hxngxd.enums.UI;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.StageManager;
import com.hxngxd.ui.UIManager;
import com.hxngxd.utils.ImageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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
    private ImageView profileImage;
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label userInfoLabel;
    @FXML
    private SplitPane root;
    @FXML
    private Button borrowButton;
    @FXML
    private Button manageButton;

    @FXML
    private void initialize() {
        User user = UserService.getInstance().getCurrentUser();
        if (user.getImage() != null) {
            setProfileImage(
                    ImageHandler.cropImageByRatio(user.getImage(), 1, 1)
            );
        }
        setFullNameLabel(user.getFullNameFirstThenLast());
        setUserInfoLabel(user.toString());

        if (user.getRole() == Role.USER) {
            manageButton.setDisable(true);
            manageButton.setVisible(false);
            manageButton.setManaged(false);
        } else {
            borrowButton.setDisable(true);
            borrowButton.setVisible(false);
            borrowButton.setManaged(false);
        }

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

    public void setProfileImage(Image profileImage) {
        this.profileImage.setFitHeight(90);
        this.profileImage.setFitWidth(90);
        this.profileImage.setPreserveRatio(true);
        Circle clip = new Circle(45, 45, 45);
        this.profileImage.setClip(clip);
        this.profileImage.setImage(profileImage);
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
        FXMLLoader loader = UIManager.loadOnce(UI.ACCOUNT);
        ((AccountController) loader.getController()).update();
        navigate(loader.getRoot());
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

    @FXML
    private void showManage(ActionEvent event) {
        StageManager.getInstance().showPopup(UI.MANAGE_POPUP);
//        if (currentTab == UI.MANAGE_USER) {
//            return;
//        }
//        FXMLLoader loader = UIManager.loadOnce(UI.MANAGE_USER);
//        navigate(loader.getRoot());
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

    public void navigate(Parent tab) {
        if (root.getItems().contains(tab)) {
            return;
        }
        AnchorPane navigation = (AnchorPane) root.getItems().getFirst();
        root.getItems().clear();
        root.getItems().addAll(navigation, tab);
    }

    public UI getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(UI currentTab) {
        this.currentTab = currentTab;
    }
}
