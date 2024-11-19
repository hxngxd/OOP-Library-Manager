package com.hxngxd.ui;

import com.hxngxd.enums.UI;
import com.hxngxd.ui.controller.scene.MainController;
import com.hxngxd.ui.controller.tab.ManageUserController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;
import java.util.Stack;

public final class PopupManager {

    @FXML
    private VBox mainVbox;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField textField;

    @FXML
    private TextArea textArea;

    private static Stack<TextInputControl> inputStack = new Stack<>();

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
        popup.addConfirmation(action, PopupManager::closePopup);
        showPeek();
    }

    public static void navigate(String message, List<Pair<String, Runnable>> btns) {
        PopupManager popup = loadPopup();
        popup.addMessage(message);
        for (Pair<String, Runnable> btn : btns) {
            popup.addNavigateButton(btn.getKey(), btn.getValue());
        }
        showPeek();
    }

    public static void confirmInput(String message, String prompt, Runnable action) {
        PopupManager popup = loadPopup();
        popup.addMessage(message);
        popup.addInput(prompt);
        popup.addConfirmation(action, () -> {
            popInput();
            closePopup();
        });
        showPeek();
    }

    public static void confirmLargeInput(String message, String prompt, String text, Runnable action) {
        PopupManager popup = loadPopup();
        popup.addMessage(message);
        popup.addTextArea(prompt, text);
        popup.addConfirmation(action, () -> {
            popInput();
            closePopup();
        });
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

    public static TextInputControl getInputPeek() {
        return inputStack.peek();
    }

    public static void popInput() {
        if (!inputStack.isEmpty()) {
            inputStack.pop();
        }
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

    private void addConfirmation(Runnable action, Runnable cancel) {
        if (!mainVbox.getChildren().contains(buttonVbox)) {
            mainVbox.getChildren().add(buttonVbox);
        }
        buttonVbox.getChildren().add(confirmation);
        getButton(confirmation, "okButton").setOnAction(event -> {
            action.run();
        });
        getButton(confirmation, "cancelButton").setOnAction(event -> {
            cancel.run();
        });
    }

    private void addInput(String prompt) {
        if (!mainVbox.getChildren().contains(textField)) {
            mainVbox.getChildren().add(textField);
        }
        textField.setPromptText(prompt);
        inputStack.push(textField);
    }

    private void addTextArea(String prompt, String text) {
        if (!mainVbox.getChildren().contains(textArea)) {
            mainVbox.getChildren().add(textArea);
        }
        textArea.setPromptText(prompt);
        textArea.setText(text);
        inputStack.push(textArea);
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
        clone.getStylesheets().add(getClass().getResource("/com/hxngxd/ui/style.css").toExternalForm());
        clone.setOnAction(event -> {
            action.run();
        });
        return clone;
    }

}