package ui.controller;

import bank.IncomingTransfer;
import bank.OutgoingTransfer;
import bank.Payment;
import bank.Transaction;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAlreadyExistException;
import bank.exceptions.TransactionAttributeException;
import bank.exceptions.TransactionDoesNotExistException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ui.dialog.TransactionDialog;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Controller für die detaillierte Kontoansicht mit sämtlichen Transaktionen.
 */
public class AccountViewController extends BaseController {

    @FXML
    private Label accountNameLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private ComboBox<ViewMode> viewModeCombo;

    @FXML
    private ListView<Transaction> transactionListView;

    private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);

    private String accountName;

    @Override
    protected void onReady() {
        transactionListView.setItems(transactions);
        viewModeCombo.getItems().setAll(ViewMode.values());
        viewModeCombo.getSelectionModel().select(ViewMode.ASCENDING);
        viewModeCombo.valueProperty().addListener((obs, old, mode) -> refreshTransactions());
        configureTransactionCells();
    }

    public void setAccount(String accountName) {
        this.accountName = accountName;
        accountNameLabel.setText(accountName);
        refreshBalance();
        refreshTransactions();
    }

    @FXML
    private void onBack() {
        try {
            sceneManager.showMainView();
        } catch (IOException ex) {
            showError("Scene-Wechsel fehlgeschlagen", ex.getMessage());
        }
    }

    @FXML
    private void onAddTransaction() {
        if (accountName == null) {
            return;
        }

        TransactionDialog dialog = new TransactionDialog(bank, accountName);
        dialog.showAndWait().ifPresent(this::persistTransaction);
    }

    private void persistTransaction(Transaction transaction) {
        try {
            bank.addTransaction(accountName, transaction);
            refreshTransactions();
            refreshBalance();
        } catch (TransactionAlreadyExistException | AccountDoesNotExistException
                 | TransactionAttributeException | IOException ex) {
            showError("Transaktion konnte nicht gespeichert werden", ex.getMessage());
        }
    }

    private void refreshBalance() {
        double balance = bank.getAccountBalance(accountName);
        balanceLabel.setText(currencyFormat.format(balance));
    }

    private void refreshTransactions() {
        if (accountName == null) {
            return;
        }

        ViewMode mode = viewModeCombo.getValue();
        List<Transaction> data;
        if (mode == null) {
            data = bank.getTransactions(accountName);
        } else {
            data = switch (mode) {
                case ASCENDING -> bank.getTransactionsSorted(accountName, true);
                case DESCENDING -> bank.getTransactionsSorted(accountName, false);
                case POSITIVE -> bank.getTransactionsByType(accountName, true);
                case NEGATIVE -> bank.getTransactionsByType(accountName, false);
            };
        }

        transactions.setAll(data);
    }

    private void configureTransactionCells() {
        transactionListView.setCellFactory(listView -> {
            ListCell<Transaction> cell = new ListCell<>() {
                @Override
                protected void updateItem(Transaction item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(buildTransactionText(item));
                    }
                }
            };

            ContextMenu menu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Löschen");
            deleteItem.setOnAction(event -> {
                if (!cell.isEmpty()) {
                    deleteTransaction(cell.getItem());
                }
            });
            menu.getItems().add(deleteItem);

            cell.emptyProperty().addListener((obs, wasEmpty, isEmpty) ->
                    cell.setContextMenu(isEmpty ? null : menu));

            return cell;
        });
    }

    private void deleteTransaction(Transaction transaction) {
        if (!showConfirmation("Transaktion löschen", "Soll die Transaktion wirklich entfernt werden?")) {
            return;
        }

        try {
            bank.removeTransaction(accountName, transaction);
            refreshTransactions();
            refreshBalance();
        } catch (AccountDoesNotExistException | TransactionDoesNotExistException | IOException ex) {
            showError("Transaktion konnte nicht gelöscht werden", ex.getMessage());
        }
    }

    private String buildTransactionText(Transaction transaction) {
        StringBuilder builder = new StringBuilder();
        builder.append(transaction.getDate()).append(" - ").append(transaction.getDescription());
        builder.append(" (").append(currencyFormat.format(transaction.calculate())).append(")");

        if (transaction instanceof Payment) {
            builder.append(" [Payment]");
        } else if (transaction instanceof IncomingTransfer) {
            builder.append(" [IncomingTransfer]");
        } else if (transaction instanceof OutgoingTransfer) {
            builder.append(" [OutgoingTransfer]");
        }
        return builder.toString();
    }

    public enum ViewMode {
        ASCENDING("Aufsteigend"),
        DESCENDING("Absteigend"),
        POSITIVE("Nur Einnahmen"),
        NEGATIVE("Nur Ausgaben");

        private final String label;

        ViewMode(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
