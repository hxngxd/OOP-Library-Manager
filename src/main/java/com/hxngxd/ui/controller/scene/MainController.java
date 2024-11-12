package com.hxngxd.ui.controller.scene;

import com.hxngxd.entities.User;
import com.hxngxd.enums.LogMessages;
import com.hxngxd.enums.Role;
import com.hxngxd.enums.UI;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.StageManager;
import com.hxngxd.ui.UIManager;
import com.hxngxd.ui.controller.tab.AccountController;
import com.hxngxd.ui.controller.tab.BookGalleryController;
import com.hxngxd.ui.controller.book.BookPreviewController;
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
import javafx.scene.shape.Circle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class MainController extends NavigateController {

    private static final Logger log = LogManager.getLogger(MainController.class);

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
        setFullNameLabel(user.getFullNameFirstThenLast());
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
        BookGalleryController.getInstance().showBookCards(null);
        showBookGallery();
    }

    @FXML
    private void showSavedBook(ActionEvent event) {
        BookGalleryController.getInstance().onActive();
        BookGalleryController.getInstance().setIsShowingSavedBook(true);
        BookGalleryController.getInstance().showBookCards(null);
        showBookGallery();
    }

    @FXML
    private void showManage(ActionEvent event) {
        StageManager.showPopup(UI.MANAGE_POPUP);
    }

    private void showBookGallery() {
        UI ui = UI.BOOK_GALLERY;
        if (currentTab == ui) {
            return;
        }
        currentTab = ui;
        navigate(UIManager.getRootOnce(ui));
        if (BookPreviewController.getInstance().isPreviewing()) {
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
        StageManager.showConfirmationPopup("ĐĂNG XUẤT?", () -> {
            try {
                UserService.getInstance().logout();
                StageManager.showInformationPopup(LogMessages.General.SUCCESS.getMSG("log out"));
                StageManager.getInstance().setScene(UI.LOGIN);
                LoginController.getInstance().onActive();
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                StageManager.showInformationPopup(e.getMessage());
            }
        });
    }

    public static MainController getInstance() {
        return UIManager.getControllerOnce(UI.MAIN);
    }

}
