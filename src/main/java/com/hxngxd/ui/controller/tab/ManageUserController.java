package com.hxngxd.ui.controller.tab;

import com.hxngxd.entities.User;
import com.hxngxd.enums.UI;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.UIManager;
import com.hxngxd.utils.Formatter;
import com.hxngxd.utils.InputHandler;
import javafx.animation.PauseTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ManageUserController {

    @FXML
    private TableView<User> userTableView;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> searchFieldComboBox;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private TableColumn<User, String> dateOfBirthColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> addressColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private TableColumn<User, String> joinDateColumn;

    @FXML
    private TableColumn<User, String> statusColumn;

    @FXML
    private TableColumn<User, String> lastActivityColumn;

    @FXML
    private TableColumn<User, Integer> violationCountColumn;

    private final ObservableList<User> userList = FXCollections.observableArrayList(UserService.userList);

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<User, Integer> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getId());
            }
        });

        firstNameColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getFirstName());
            }
        });

        lastNameColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getLastName());
            }
        });

        dateOfBirthColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                LocalDate dob = param.getValue().getDateOfBirth();
                String formattedDate = (dob != null) ? Formatter.formatDateDash(dob) : null;
                return new ReadOnlyObjectWrapper<>(formattedDate);
            }
        });

        usernameColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getUsername());
            }
        });

        emailColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getEmail());
            }
        });

        addressColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getAddress());
            }
        });

        roleColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getRole() != null ? param.getValue().getRole().name() : null);
            }
        });

        joinDateColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                LocalDateTime joinDate = param.getValue().getDateAdded();
                String formattedJoinDate = joinDate != null ? Formatter.formatDateTime(joinDate) : null;
                return new ReadOnlyObjectWrapper<>(formattedJoinDate);
            }
        });

        statusColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getAccountStatus().name());
            }
        });

        lastActivityColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                LocalDateTime lastActivity = param.getValue().getLastUpdated();
                String formattedLastActivity = lastActivity != null ? Formatter.formatDateTime(lastActivity) : "Chưa hoạt động";
                return new ReadOnlyObjectWrapper<>(
                        param.getValue().getId() == UserService.getInstance().getCurrentUser().getId()
                                ? "Đang hoạt động" : formattedLastActivity);
            }
        });

        violationCountColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<User, Integer> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getViolationCount());
            }
        });

        searchFieldComboBox.setItems(FXCollections.observableArrayList(
                idColumn.getText(),
                lastNameColumn.getText(),
                firstNameColumn.getText(),
                dateOfBirthColumn.getText(),
                usernameColumn.getText(),
                emailColumn.getText(),
                addressColumn.getText(),
                roleColumn.getText(),
                joinDateColumn.getText(),
                statusColumn.getText()
        ));
        searchFieldComboBox.setValue(idColumn.getText());

        PauseTransition pause = new PauseTransition(Duration.millis(250));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 127) {
                searchField.setText(newValue.substring(0, 127));
            } else {
                pause.setOnFinished(event -> filterUsers(newValue));
                pause.playFromStart();
            }
        });
    }

    private void filterUsers(String searchText) {
        String selectedField = searchFieldComboBox.getValue();

        FilteredList<User> filteredData = new FilteredList<>(userList, user -> {
            if (searchText.isEmpty()) {
                return true;
            }

            return switch (selectedField) {
                case "ID" -> String.valueOf(user.getId()).equals(searchText);

                case "Họ và tên đệm" -> user.getLastName() != null
                        && InputHandler.isUnidecodeSimilar(user.getLastName(), searchText);

                case "Tên" -> user.getFirstName() != null
                        && InputHandler.isUnidecodeSimilar(user.getFirstName(), searchText);

                case "Ngày sinh" -> user.getDateOfBirth() != null
                        && Formatter.formatDateDash(user.getDateOfBirth()).contains(searchText);

                case "Username" -> user.getUsername() != null
                        && InputHandler.isSimilar(user.getUsername(), searchText);

                case "Email" -> user.getEmail() != null
                        && InputHandler.isSimilar(user.getEmail(), searchText);

                case "Địa chỉ" -> user.getAddress() != null
                        && InputHandler.isUnidecodeSimilar(user.getAddress(), searchText);

                case "Vai trò" -> user.getRole() != null
                        && InputHandler.lowerPrefixMatching(
                        user.getRole().name(), searchText);

                case "Ngày tham gia" -> user.getDateAdded() != null
                        && Formatter.formatDateTime(user.getDateAdded()).contains(searchText);

                case "Trạng thái" -> user.getAccountStatus() != null
                        && InputHandler.lowerPrefixMatching(
                        user.getAccountStatus().name(), searchText);

                default -> false;
            };
        });

        userTableView.setItems(filteredData);
    }

    public void loadUsers() {
        userTableView.setItems(userList);
    }

    @FXML
    private void changeRole() {
        User currentSelected = userTableView.getSelectionModel().getSelectedItem();
        System.out.println(currentSelected.getId());
    }

    public static ManageUserController getInstance() {
        return UIManager.getControllerOnce(UI.MANAGE_USER);
    }

}
