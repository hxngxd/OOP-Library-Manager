package com.hxngxd.ui.controller.manage;

import com.hxngxd.actions.Borrowing;
import com.hxngxd.enums.LogMsg;
import com.hxngxd.enums.UI;
import com.hxngxd.ui.UIManager;
import com.hxngxd.ui.controller.NavigateController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ManageBorrow0Controller extends NavigateController {

    private final List<FXMLLoader> borrowCards = new ArrayList<>();

    @FXML
    private VBox borrowCardContainer;

    @FXML
    private Label header;

    @Override
    public void onActive() {
        borrowCards.clear();
        borrowCardContainer.getChildren().clear();
        borrowCardContainer.getChildren().add(header);

        for (Borrowing borrowing : Borrowing.borrowingSet) {
            try {
                FXMLLoader loader = UIManager.load(UI.BORROW_CARD);
                borrowCards.add(loader);
                ((BorrowCardController) loader.getController()).setBook(borrowing.getBook());
                ((BorrowCardController) loader.getController()).setBorrowing(borrowing);
                borrowCardContainer.getChildren().add(loader.getRoot());
            } catch (NullPointerException e) {
                log.error(LogMsg.GENERAL_FAIL.msg("load borrow card"), e);
            }
        }
    }

}
