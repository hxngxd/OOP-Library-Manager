package com.hxngxd.ui.controller.manage;

import com.hxngxd.entities.Entity;
import com.hxngxd.utils.Formatter;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.time.LocalDateTime;

public abstract class EntityManageController<T extends Entity> extends ManageController<T> {

    @FXML
    protected TableColumn<T, String> lastUpdatedColumn;

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

    @Override
    protected int getSelectedId() {
        return (getSelected() == null ? -1 : getSelected().getId());
    }

}
