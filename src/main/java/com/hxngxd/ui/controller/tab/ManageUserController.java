package com.hxngxd.ui.controller.tab;

import com.hxngxd.entities.User;
import com.hxngxd.enums.AccountStatus;
import com.hxngxd.enums.Role;
import com.hxngxd.enums.UI;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.exceptions.UserException;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.ui.UIManager;
import com.hxngxd.utils.Formatter;
import com.hxngxd.utils.InputHandler;
import javafx.animation.PauseTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.util.Duration;

import java.time.LocalDate;

public final class ManageUserController extends ManageController<User> {

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
    private TableColumn<User, String> statusColumn;

    private final UserService userService = UserService.getInstance();

    @Override
    @FXML
    public void initialize() {
        super.initialize();

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


        statusColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getAccountStatus().name());
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
                dateAddedColumn.getText(),
                statusColumn.getText()
        ));
        searchFieldComboBox.setValue(idColumn.getText());

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

    @Override
    protected void filterItems() {
        String selectedField = searchFieldComboBox.getValue();
        String searchText = InputHandler.unidecode(searchField.getText());

        FilteredList<User> filteredData = new FilteredList<>(itemList, user -> {
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
        itemTableView.setItems(filteredData);
    }

    @Override
    @FXML
    public void update() {
        try {
            userService.getAllUsers();
            itemList.clear();
            itemList = FXCollections.observableArrayList(UserService.userList);
            super.update();
        } catch (DatabaseException e) {
            PopupManager.info("CẬP NHẬT DANH SÁCH NGƯỜI DÙNG THẤT BẠI!");
        }
    }

    @FXML
    private void changePassword() {
        if (getSelected() == null) {
            noneSelected();
            return;
        }
        String message = String.format("Đổi mật khẩu user có id=%d?", getSelectedId());
        PopupManager.confirmInput(message, "Mật khẩu mới", () -> {
            String newPassword = PopupManager.getInputPeek().getText();
            try {
                userService.changePassword(getSelectedId(), newPassword);
                PopupManager.popInput();
                PopupManager.closePopup();
                update();
            } catch (DatabaseException | UserException e) {
                PopupManager.info(e.getMessage());
            }
        });
    }

    @FXML
    private void releaseUser() {
        changeAccountStatus(AccountStatus.INACTIVE, "thả");
    }

    @FXML
    private void suspendUser() {
        changeAccountStatus(AccountStatus.SUSPENDED, "khoá");
    }

    @FXML
    private void banUser() {
        changeAccountStatus(AccountStatus.BANNED, "cấm vĩnh viễn");
    }

    @FXML
    private void deleteUser() {
        if (getSelected() == null || getSelectedId() == userService.getCurrentUser().getId()) {
            noneSelected();
            return;
        }
        String message = String.format("Xác nhận xoá user có id=%d (không thể hoàn tác)", getSelectedId());
        PopupManager.confirm(message, () -> {
            try {
                userService.deleteAccount(getSelectedId());
                update();
            } catch (DatabaseException | UserException e) {
                PopupManager.info(e.getMessage());
            } finally {
                PopupManager.closePopup();
            }
        });
    }

    private void changeAccountStatus(AccountStatus status, String action) {
        if (getSelected() == null || getSelected().getAccountStatus() == status) {
            noneSelected();
            return;
        }
        String message = String.format("Xác nhận %s user có id=%d?", action, getSelectedId());
        PopupManager.confirm(
                message, () -> {
                    try {
                        userService.changeAccountStatus(getSelectedId(), status);
                        update();
                    } catch (DatabaseException | UserException e) {
                        PopupManager.info(e.getMessage());
                    } finally {
                        PopupManager.closePopup();
                    }
                }
        );
    }

    @FXML
    private void changeRoleToUser() {
        changeRole(Role.USER);
    }

    @FXML
    private void changeRoleToModerator() {
        changeRole(Role.MODERATOR);
    }

    private void changeRole(Role role) {
        if (getSelected() == null || getSelected().getRole() == role) {
            noneSelected();
            return;
        }
        String message = String.format("Xác nhận đổi vai trò của user có id=%d thành %s?", getSelectedId(), role.name());
        PopupManager.confirm(
                message, () -> {
                    try {
                        userService.changeRole(getSelectedId(), role);
                        update();
                    } catch (DatabaseException | UserException e) {
                        PopupManager.info(e.getMessage());
                    } finally {
                        PopupManager.closePopup();
                    }
                }
        );
    }

    public static ManageUserController getInstance() {
        return UIManager.getControllerOnce(UI.MANAGE_USER);
    }

}
