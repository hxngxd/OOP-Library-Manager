package com.hxngxd.ui;

import com.hxngxd.enums.UI;
import com.hxngxd.ui.controller.popup.ConfirmationPopupController;
import com.hxngxd.ui.controller.popup.InformationPopupController;
import com.hxngxd.ui.controller.popup.ManagePopupController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Stack;

public final class StageManager {

    public static final Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

    private final double widthRatio = 0.775;

    private final double heightRatio = 0.85;

    private static final StageManager stageManager = StageManager.getInstance();

    private Stage mainStage;

    public static Stack<Stage> stageStack = new Stack<>();

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
            stageStack.push(mainStage);
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

    public static void closeMainStage() {
        stageManager.mainStage.close();
    }

    public static void showInfoPopup(String info) {
        FXMLLoader loader = loadPopup(UI.INFORMATION_POPUP);
        ((InformationPopupController) loader.getController()).setInformationLabel(info);
        stageStack.peek().showAndWait();
    }

    public static void showConfirmationPopup(String info, Runnable action) {
        FXMLLoader loader = loadPopup(UI.CONFIRMATION_POPUP);
        ConfirmationPopupController cpc = loader.getController();
        cpc.setInformationLabel(info);
        cpc.setAction(action);
        stageStack.peek().showAndWait();
    }

    public static void showManagePopup() {
        loadPopup(UI.MANAGE_POPUP);
        stageStack.peek().showAndWait();
    }

    private static FXMLLoader loadPopup(UI ui) {
        FXMLLoader loader = UIManager.load(ui);

        Stage popupStage = new Stage();
        if (!stageStack.isEmpty()) {
            Stage ownerStage = stageStack.peek();
            popupStage.initOwner(ownerStage);
            ownerStage.getScene().getRoot().setDisable(true);
        }
        popupStage.setScene(new Scene(loader.getRoot()));
        popupStage.sizeToScene();
        popupStage.setOnShown(event -> {
            popupStage.setX((screenSize.getWidth() - popupStage.getWidth()) / 2);
            popupStage.setY((screenSize.getHeight() - popupStage.getHeight()) / 2);
        });
        popupStage.setResizable(false);
        popupStage.setOnCloseRequest(Event::consume);
        stageStack.push(popupStage);

        return loader;
    }

    public static void closePopup() {
        if (!stageStack.isEmpty()) {
            Stage popupStage = stageStack.pop();
            if (popupStage.isShowing()) {
                popupStage.close();
            }
            popupStage = null;
            if (!stageStack.isEmpty()) {
                Stage ownerStage = stageStack.peek();
                ownerStage.getScene().getRoot().setDisable(false);
            }
        }
    }

}