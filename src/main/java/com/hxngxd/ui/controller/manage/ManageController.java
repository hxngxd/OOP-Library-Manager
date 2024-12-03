package com.hxngxd.ui.controller.manage;

import com.hxngxd.enums.LogMsg;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.ui.Updatable;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public abstract class ManageController<T> implements Updatable {

    @FXML
    protected TableView<T> itemTableView;

    @FXML
    protected TextField searchField;

    @FXML
    protected ComboBox<String> searchFieldComboBox;

    @FXML
    protected TableColumn<T, Integer> idColumn;

    @FXML
    protected TableColumn<T, String> dateAddedColumn;

    protected ObservableList<T> itemList = FXCollections.observableArrayList();

    protected abstract void filterItems();

    protected abstract void addSearchFields();

    @FXML
    public void onUpdate() {
        searchField.setText("");
        itemTableView.setItems(itemList);
        PopupManager.info(LogMsg.GENERAL_SUCCESS.msg("update"));
    }

    protected void searchItems() {
        PauseTransition pause = new PauseTransition(Duration.millis(250));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 127) {
                searchField.setText(newValue.substring(0, 127));
            } else {
                pause.setOnFinished(event -> filterItems());
                pause.playFromStart();
            }
        });
    }

    protected T getSelected() {
        T item = itemTableView.getSelectionModel().getSelectedItem();
        if (item == null) {
            PopupManager.info("None is being selected");
        }
        return item;
    }

    protected abstract int getSelectedId();

}
