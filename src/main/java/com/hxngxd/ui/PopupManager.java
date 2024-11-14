package com.hxngxd.ui;

import com.hxngxd.enums.UI;
import com.hxngxd.ui.controller.scene.MainController;
import com.hxngxd.ui.controller.tab.ManageUserController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class PopupManager {

    @FXML
    private VBox mainVbox;

    @FXML
    private Label messageLabel;

    @FXML
    private VBox buttonVbox;

    @FXML
    private HBox confirmation;

    @FXML
    private Button navigate;

    public static void info(String message) {
        PopupManager popup = loadPopup();
        popup.addMessage(message);
        popup.addNavigateButton("OK", PopupManager::closePopup);
        showPeek();
    }

    public static void confirm(String message, Runnable action) {
        PopupManager popup = loadPopup();
        popup.addMessage(message);
        popup.addConfirmation(action);
        showPeek();
    }

    public static void navigate(String message) {
        PopupManager popup = loadPopup();
        popup.addMessage(message);
        popup.addNavigateButton("NGƯỜI DÙNG", () -> {
            MainController mainController = MainController.getInstance();
            UI ui = UI.MANAGE_USER;
            if (mainController.getCurrentTab() != ui) {
                mainController.setCurrentTab(ui);
                mainController.navigate(UIManager.getRootOnce(ui));
                ManageUserController.getInstance().update();
            }
            closePopup();
        });
        popup.addNavigateButton("SÁCH", () -> {
        });
        popup.addNavigateButton("MƯỢN SÁCH", () -> {
        });
        popup.addNavigateButton("TÁC GIẢ", () -> {
        });
        popup.addNavigateButton("THỂ LOẠI", () -> {
        });
        popup.addNavigateButton("HUỶ", PopupManager::closePopup);
        showPeek();
    }

    private static PopupManager loadPopup() {
        FXMLLoader loader = UIManager.load(UI.POPUP);

        Stage popupStage = new Stage();
        if (!StageManager.stageStack.isEmpty()) {
            Stage ownerStage = StageManager.stageStack.peek();
            popupStage.initOwner(ownerStage);
        }
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.setScene(new Scene(loader.getRoot()));
        popupStage.sizeToScene();
        popupStage.setOnShown(event -> {
            popupStage.setX((StageManager.screenSize.getWidth() - popupStage.getWidth()) / 2);
            popupStage.setY((StageManager.screenSize.getHeight() - popupStage.getHeight()) / 2);
        });
        popupStage.setResizable(false);
        popupStage.setOnCloseRequest(Event::consume);
        StageManager.stageStack.push(popupStage);

        ((PopupManager) loader.getController()).mainVbox.getChildren().clear();
        ((PopupManager) loader.getController()).buttonVbox.getChildren().clear();
        return loader.getController();
    }

    private static void showPeek() {
        StageManager.stageStack.peek().showAndWait();
    }

    public static void closePopup() {
        if (!StageManager.stageStack.isEmpty()) {
            Stage popupStage = StageManager.stageStack.pop();
            if (popupStage != null && popupStage.isShowing()) {
                popupStage.close();
            }
        }
    }

    private static Button getButton(Pane pane, String id) {
        return (Button) pane.lookup("#" + id);
    }

    private void addMessage(String message) {
        messageLabel.setText(message);
        mainVbox.getChildren().add(messageLabel);
    }

    private void addNavigateButton(String text, Runnable action) {
        if (!mainVbox.getChildren().contains(buttonVbox)) {
            mainVbox.getChildren().add(buttonVbox);
        }
        Button clone = cloneButton(navigate, text, action);
        buttonVbox.getChildren().add(clone);
        VBox.setVgrow(clone, Priority.ALWAYS);
    }

    private void addConfirmation(Runnable action) {
        if (!mainVbox.getChildren().contains(buttonVbox)) {
            mainVbox.getChildren().add(buttonVbox);
        }
        buttonVbox.getChildren().add(confirmation);
        getButton(confirmation, "okButton").setOnAction(event -> {
            action.run();
            PopupManager.closePopup();
        });
        getButton(confirmation, "cancelButton").setOnAction(event -> closePopup());
    }

    private Button cloneButton(Button original, String text, Runnable action) {
        Button clone = new Button();
        clone.setText(text);
        clone.setDisable(original.isDisable());
        clone.setMinWidth(original.getMinWidth());
        clone.setMinHeight(original.getMinHeight());
        clone.setPrefWidth(original.getPrefWidth());
        clone.setPrefHeight(original.getPrefHeight());
        clone.setMaxWidth(original.getMaxWidth());
        clone.setMaxHeight(original.getMaxHeight());
        clone.setStyle(original.getStyle());
        clone.getStyleClass().setAll(original.getStyleClass());
        clone.setOnAction(event -> {
            action.run();
        });
        return clone;
    }

}