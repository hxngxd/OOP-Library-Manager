package com.hxngxd.ui.controller.manage;

import com.hxngxd.entities.User;
import com.hxngxd.enums.AccountStatus;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.enums.Role;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.exceptions.UserException;
import com.hxngxd.service.UserService;
import com.hxngxd.ui.PopupManager;
import com.hxngxd.utils.Formatter;
import com.hxngxd.utils.InputHandler;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.time.LocalDate;

public final class ManageUserController extends EntityManageController<User> {

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
    protected void initialize() {
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
    protected void addSearchFields() {
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
    }

    @Override
    @FXML
    public void onUpdate() {
        try {
            userService.loadAll();
            itemList.clear();
            itemList = FXCollections.observableArrayList(User.userSet);
            super.onUpdate();
        } catch (DatabaseException e) {
            PopupManager.info(LogMsg.GENERAL_FAIL.msg("update user list"));
        }
    }

    @FXML
    private void changePassword() {
        if (getSelected() == null) {
            return;
        }
        String message = String.format("Change password of user with id=%d?", getSelectedId());
        PopupManager.confirmInput(message, "New password", () -> {
            String newPassword = PopupManager.getInputPeek().getText();
            try {
                userService.changePassword(getSelectedId(), newPassword);
                PopupManager.popInput();
                PopupManager.closePopup();
                onUpdate();
            } catch (DatabaseException | UserException e) {
                PopupManager.info(e.getMessage());
            }
        });
    }

    @FXML
    private void releaseUser() {
        changeAccountStatus(AccountStatus.INACTIVE);
    }

    @FXML
    private void suspendUser() {
        changeAccountStatus(AccountStatus.SUSPENDED);
    }

    @FXML
    private void banUser() {
        changeAccountStatus(AccountStatus.BANNED);
    }

    @FXML
    private void deleteUser() {
        if (getSelected() == null) {
            return;
        }
        String message = "You sure about removing this user (can't be undone)?";
        PopupManager.confirm(message, () -> {
            try {
                userService.deleteAccount(getSelectedId());
                onUpdate();
            } catch (DatabaseException | UserException e) {
                PopupManager.info(e.getMessage());
            } finally {
                PopupManager.closePopup();
            }
        });
    }

    private void changeAccountStatus(AccountStatus status) {
        if (getSelected() == null) {
            return;
        }
        if (getSelected().getAccountStatus() == status) {
            PopupManager.info("User has already been " + status.name());
            return;
        }
        String message = "Change user's status?";
        PopupManager.confirm(
                message, () -> {
                    try {
                        userService.changeAccountStatus(getSelectedId(), status);
                        onUpdate();
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
        if (getSelected() == null) {
            return;
        }
        if (getSelected().getRole() == role) {
            PopupManager.info("User is already " + role.name());
            return;
        }
        String message = "Change user's role?";
        PopupManager.confirm(
                message, () -> {
                    try {
                        userService.changeRole(getSelectedId(), role);
                        onUpdate();
                    } catch (DatabaseException | UserException e) {
                        PopupManager.info(e.getMessage());
                    } finally {
                        PopupManager.closePopup();
                    }
                }
        );
    }

}
