package com.hxngxd.ui.controller.manage;

import com.hxngxd.entities.Entity;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.ui.Updatable;
import com.hxngxd.utils.Formatter;
import javafx.animation.PauseTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.Duration;

import java.time.LocalDateTime;

public abstract class ManageController<T extends Entity> implements Updatable {

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

    @FXML
    protected TableColumn<T, String> lastUpdatedColumn;

    protected ObservableList<T> itemList = FXCollections.observableArrayList();

    @FXML
    protected void initialize() {
        idColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<T, Integer> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getId());
            }
        });

        dateAddedColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<T, String> param) {
                LocalDateTime dateAdded = param.getValue().getDateAdded();
                return new ReadOnlyObjectWrapper<>(
                        dateAdded != null ? Formatter.formatDateTime(dateAdded) : null
                );
            }
        });

        lastUpdatedColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<T, String> param) {
                LocalDateTime lastUpdated = param.getValue().getLastUpdated();
                return new ReadOnlyObjectWrapper<>(
                        lastUpdated != null ? Formatter.formatDateTime(lastUpdated)
                                : Formatter.formatDateTime(param.getValue().getDateAdded())
                );
            }
        });

        searchItems();
        addSearchFields();
    }

    private void searchItems() {
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

    protected abstract void filterItems();

    protected abstract void addSearchFields();

    @FXML
    public void onUpdate() {
        searchField.setText("");
        itemTableView.setItems(itemList);
        PopupManager.info(LogMsg.GENERAL_SUCCESS.msg("update"));
    }

    protected T getSelected() {
        T item = itemTableView.getSelectionModel().getSelectedItem();
        if (item == null) {
            PopupManager.info("None is being selected");
        }
        return item;
    }

    protected int getSelectedId() {
        return (getSelected() == null ? -1 : getSelected().getId());
    }

}
