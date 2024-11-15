package com.hxngxd.ui.controller.tab;

import com.hxngxd.entities.Entity;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.utils.Formatter;
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

import java.time.LocalDateTime;

public abstract class ManageController<T extends Entity> {

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
    }

    protected abstract void filterItems();

    protected void loadItems() {
        itemTableView.setItems(itemList);
    }

    @FXML
    public void update() {
        searchField.setText("");
        loadItems();
        PopupManager.info("Cập nhật thành công!");
    }

    protected T getSelected() {
        return itemTableView.getSelectionModel().getSelectedItem();
    }

    protected int getSelectedId() {
        return itemTableView.getSelectionModel().getSelectedItem().getId();
    }

}
