package com.hxngxd.ui;

import com.hxngxd.enums.UI;
import com.hxngxd.ui.controller.popup.ConfirmationPopupController;
import com.hxngxd.ui.controller.popup.InformationPopupController;
import javafx.geometry.Rectangle2D;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public final class StageManager {

    private static final Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

    private final double widthRatio = 0.775;

    private final double heightRatio = 0.85;

    private static final StageManager stageManager = StageManager.getInstance();

    private Stage mainStage;

    private Stage popupStage;

    private StageManager() {
    }

    private static class SingletonHolder {
        private static final StageManager instance = new StageManager();
    }

    public static StageManager getInstance() {
        return SingletonHolder.instance;
    }

    public void initialize(Stage stage) {
        if (this.mainStage == null) {
            this.mainStage = stage;
        }
        setTitle("QUẢN LÝ THƯ VIỆN CĂNG NHẤT 2024");
        setWidth(widthRatio, widthRatio);
        setHeight(heightRatio, heightRatio);
        centerStage();
        setScene(UI.LOGIN);
    }

    public void setScene(UI ui) {
        this.mainStage.setScene(UIManager.loadScene(ui));
        this.mainStage.show();
        UIManager.currentScene = ui;
    }

    public void setWidth(double minRatio, double prefRatio) {
        this.mainStage.setWidth(screenSize.getWidth() * prefRatio);
        this.mainStage.setMinWidth(screenSize.getWidth() * minRatio);
    }

    public void setHeight(double minRatio, double prefRatio) {
        this.mainStage.setHeight(screenSize.getHeight() * prefRatio);
        this.mainStage.setMinHeight(screenSize.getHeight() * minRatio);
    }

    public void setTitle(String title) {
        this.mainStage.setTitle(title);
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void centerStage() {
        this.mainStage.setX((screenSize.getWidth() - this.mainStage.getWidth()) / 2);
        this.mainStage.setY((screenSize.getHeight() - this.mainStage.getHeight()) / 2);
    }

    public static void showPopup(UI ui) {
        closePopupStage();
        stageManager.popupStage = new Stage();
        stageManager.popupStage.initModality(Modality.WINDOW_MODAL);
        stageManager.popupStage.initOwner(stageManager.mainStage);
        stageManager.popupStage.setScene(UIManager.loadScene(ui));
        stageManager.popupStage.getIcons().clear();
        stageManager.popupStage.sizeToScene();
        stageManager.popupStage.setOnShown(event -> {
            stageManager.popupStage.setX((screenSize.getWidth() - stageManager.popupStage.getWidth()) / 2);
            stageManager.popupStage.setY((screenSize.getHeight() - stageManager.popupStage.getHeight()) / 2);
        });
        stageManager.popupStage.showAndWait();
    }

    public static void showInformationPopup(String information) {
        InformationPopupController informationPopupController = (InformationPopupController)
                UIManager.loadOnce(UI.INFORMATION_POPUP).getController();
        informationPopupController.setInformationLabel(information);
        showPopup(UI.INFORMATION_POPUP);
    }

    public static void showConfirmationPopup(String information, Runnable action) {
        ConfirmationPopupController confirmationPopupController = (ConfirmationPopupController)
                UIManager.loadOnce(UI.CONFIRMATION_POPUP).getController();
        confirmationPopupController.setInformationLabel(information);
        confirmationPopupController.setAction(action);
        showPopup(UI.CONFIRMATION_POPUP);
    }

    public static void closeMainStage() {
        stageManager.closeStage(stageManager.mainStage);
    }

    public static void closePopupStage() {
        stageManager.closeStage(stageManager.popupStage);
    }

    private void closeStage(Stage stage) {
        if (stage != null && stage.isShowing()) {
            stage.close();
            stage = null;
        }
    }

}