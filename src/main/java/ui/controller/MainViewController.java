package ui.controller;

import bank.exceptions.AccountAlreadyExistException;
import bank.exceptions.AccountDoesNotExistException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;

import java.io.IOException;

/**
 * Controller für die Hauptansicht, die alle Bankkonten auflistet.
 */
public class MainViewController extends BaseController {

    @FXML
    private ListView<String> accountListView;

    private final ObservableList<String> accounts = FXCollections.observableArrayList();

    @Override
    protected void onReady() {
        accountListView.setItems(accounts);
        accountListView.setPlaceholder(new Label("Keine Accounts vorhanden"));
        configureContextMenu();
        loadAccounts();

        accountListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                openSelectedAccount();
            }
        });
    }

    @FXML
    private void onAddAccount(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Account hinzufügen");
        dialog.setHeaderText("Neuen Account erstellen");
        dialog.setContentText("Account-Name:");

        dialog.showAndWait()
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .ifPresent(this::createAccount);
    }

    private void createAccount(String name) {
        try {
            bank.createAccount(name);
            loadAccounts();
        } catch (AccountAlreadyExistException | IOException ex) {
            showError("Account kann nicht erstellt werden", ex.getMessage());
        }
    }

    private void configureContextMenu() {
        accountListView.setCellFactory(listView -> {
            ListCell<String> cell = new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item);
                }
            };

            ContextMenu menu = new ContextMenu();
            MenuItem selectItem = new MenuItem("Auswählen");
            selectItem.setOnAction(event -> {
                if (!cell.isEmpty()) {
                    openAccount(cell.getItem());
                }
            });
            MenuItem deleteItem = new MenuItem("Löschen");
            deleteItem.setOnAction(event -> {
                if (!cell.isEmpty()) {
                    deleteAccount(cell.getItem());
                }
            });
            menu.getItems().addAll(selectItem, deleteItem);

            cell.emptyProperty().addListener((obs, wasEmpty, isEmpty) ->
                    cell.setContextMenu(isEmpty ? null : menu));

            return cell;
        });
    }

    private void loadAccounts() {
        accounts.setAll(bank.getAllAccounts());
    }

    private void openSelectedAccount() {
        String selected = accountListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openAccount(selected);
        }
    }

    private void openAccount(String account) {
        try {
            sceneManager.showAccountView(account);
        } catch (IOException ex) {
            showError("Scene-Wechsel fehlgeschlagen", ex.getMessage());
        }
    }

    private void deleteAccount(String account) {
        if (!showConfirmation("Account löschen", "Soll der Account '" + account + "' gelöscht werden?")) {
            return;
        }

        try {
            bank.deleteAccount(account);
            loadAccounts();
        } catch (AccountDoesNotExistException | IOException ex) {
            showError("Account konnte nicht gelöscht werden", ex.getMessage());
        }
    }
}
