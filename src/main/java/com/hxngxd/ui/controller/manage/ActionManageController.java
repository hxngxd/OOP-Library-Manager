package com.hxngxd.ui.controller.manage;

import com.hxngxd.actions.Action;
import com.hxngxd.utils.Formatter;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.time.LocalDateTime;

public abstract class ActionManageController<T extends Action> extends ManageController<T> {

    @FXML
    private TableColumn<T, String> userColumn;

    @FXML
    protected void initialize() {
        idColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<T, Integer> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getId());
            }
        });

        userColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<T, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getUser().getFullNameLastThenFirst());
            }
        });

        dateAddedColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<T, String> param) {
                LocalDateTime dateAdded = param.getValue().getTimestamp();
                return new ReadOnlyObjectWrapper<>(
                        dateAdded != null ? Formatter.formatDateTime(dateAdded) : null
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
