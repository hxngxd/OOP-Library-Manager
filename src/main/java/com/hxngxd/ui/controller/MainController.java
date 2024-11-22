package com.hxngxd.ui.controller;

import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.enums.Role;
import com.hxngxd.enums.UI;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.exceptions.UserException;
import com.hxngxd.service.BookService;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.StageManager;
import com.hxngxd.ui.UIManager;
import com.hxngxd.ui.controller.book.BookPreviewController;
import com.hxngxd.ui.controller.manage.ManageBookController;
import com.hxngxd.ui.controller.manage.ManageUserController;
import com.hxngxd.utils.ImageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public final class MainController extends NavigateController {

    private static UI currentTab = null;

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
        showBookGallery();
    }

    public void onActive() {
        User user = UserService.getInstance().getCurrentUser();
        if (user.getImage() != null) {
            setProfileImage(
                    ImageHandler.cropImageByRatio(user.getImage(), 1, 1)
            );
        } else {
            setProfileImage(null);
        }
        setFullNameLabel(user.getFullNameLastThenFirst());
        setUserInfoLabel(user.toString());

        boolean isUSER = user.getRole() == Role.USER;

        manageButton.setDisable(isUSER);
        manageButton.setVisible(!isUSER);
        manageButton.setManaged(!isUSER);

        borrowButton.setDisable(!isUSER);
        borrowButton.setVisible(isUSER);
        borrowButton.setManaged(isUSER);

        showHome(null);
    }

    public void setProfileImage(Image profileImage) {
        ImageHandler.circleCrop(this.profileImage, 90);
        this.profileImage.setImage(profileImage);
    }

    public void setFullNameLabel(String fullname) {
        this.fullNameLabel.setText(fullname);
    }

    public void setUserInfoLabel(String info) {
        this.userInfoLabel.setText(info);
    }

    @FXML
    private void showAccount() {
        UI ui = UI.ACCOUNT;
        if (currentTab == ui) {
            return;
        }
        currentTab = ui;
        navigate(UIManager.getRootOnce(ui));
        AccountController.getInstance().onActive();
    }

    @FXML
    private void showHome(ActionEvent event) {
        BookGalleryController.getInstance().onActive();
        BookGalleryController.getInstance().setIsShowingSavedBook(false);
        BookGalleryController.getInstance().showBookCards();
        showBookGallery();
    }

    @FXML
    private void showSavedBook(ActionEvent event) {
        BookGalleryController.getInstance().onActive();
        BookGalleryController.getInstance().setIsShowingSavedBook(true);
        BookGalleryController.getInstance().showBookCards();
        showBookGallery();
    }

    @FXML
    private void showManage(ActionEvent event) {
        List<Pair<String, Runnable>> btns = new ArrayList<>();
        btns.add(new Pair<>("NGƯỜI DÙNG", () -> {
            UI ui = UI.MANAGE_USER;
            PopupManager.closePopup();
            if (currentTab != ui) {
                currentTab = ui;
                navigate(UIManager.getRootOnce(ui));
                ManageUserController.getInstance().update();
            }
        }));
        btns.add(new Pair<>("SÁCH", () -> {
            UI ui = UI.MANAGE_BOOK;
            PopupManager.closePopup();
            if (currentTab != ui) {
                currentTab = ui;
                navigate(UIManager.getRootOnce(ui));
                ManageBookController.getInstance().update();
            }
        }));
        btns.add(new Pair<>("MƯỢN SÁCH", () -> {
        }));
        btns.add(new Pair<>("TÁC GIẢ", () -> {
        }));
        btns.add(new Pair<>("HUỶ", PopupManager::closePopup));
        PopupManager.navigate("QUẢN LÝ\n (MOD, ADMIN)", btns);
    }

    private void showBookGallery() {
        UI ui = UI.BOOK_GALLERY;
        if (currentTab == ui) {
            return;
        }
        currentTab = ui;
        navigate(UIManager.getRootOnce(ui));
        BookPreviewController bpc = BookPreviewController.getInstance();
        if (bpc.isPreviewing() && BookService.bookMap.get(bpc.getBook().getId()) != null) {
            root.getItems().add(UIManager.getRootOnce(UI.BOOK_PREVIEW));
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
        MainController.currentTab = currentTab;
    }

    @FXML
    private void logOut() {
        PopupManager.confirm("Log out?", () -> {
            try {
                userService.logout();
                PopupManager.info(LogMsg.GENERAL_SUCCESS.msg("log out"));
                StageManager.getInstance().setScene(UI.LOGIN);
                UIManager.getControllerOnce(UI.LOGIN).onActive();
            } catch (DatabaseException | UserException e) {
                log.error(e);
                PopupManager.info(e.getMessage());
            } finally {
                PopupManager.closePopup();
            }
        });
    }

}
