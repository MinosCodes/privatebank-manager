package ui.dialog;

import bank.IncomingTransfer;
import bank.OutgoingTransfer;
import bank.Payment;
import bank.PrivateBank;
import bank.Transaction;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;

/**
 * Custom dialog that allows the user to create either a Payment or Transfer transaction.
 */
public class TransactionDialog extends Dialog<Transaction> {

    private enum TransactionType { PAYMENT, TRANSFER }

    private final PrivateBank bank;
    private final String accountName;

    private final DatePicker datePicker = new DatePicker();
    private final TextField amountField = new TextField();
    private final TextField descriptionField = new TextField();
    private final ChoiceBox<TransactionType> typeChoice = new ChoiceBox<>();
    private final TextField senderField = new TextField();
    private final TextField recipientField = new TextField();

    public TransactionDialog(PrivateBank bank, String accountName) {
        this.bank = bank;
        this.accountName = accountName;

        setTitle("Neue Transaktion");
        setHeaderText("Transaktion für " + accountName);

        datePicker.setValue(LocalDate.now());

        typeChoice.getItems().setAll(TransactionType.PAYMENT, TransactionType.TRANSFER);
        typeChoice.getSelectionModel().select(TransactionType.PAYMENT);

        senderField.setPromptText("Sender");
        recipientField.setPromptText("Empfänger");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(new Label("Datum"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Betrag"), 0, 1);
        grid.add(amountField, 1, 1);
        grid.add(new Label("Beschreibung"), 0, 2);
        grid.add(descriptionField, 1, 2);
        grid.add(new Label("Typ"), 0, 3);
        grid.add(typeChoice, 1, 3);
        grid.add(new Label("Sender"), 0, 4);
        grid.add(senderField, 1, 4);
        grid.add(new Label("Empfänger"), 0, 5);
        grid.add(recipientField, 1, 5);

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        Node okButton = getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, this::validateBeforeClose);

        senderField.disableProperty().bind(typeChoice.valueProperty().isEqualTo(TransactionType.PAYMENT));
        recipientField.disableProperty().bind(typeChoice.valueProperty().isEqualTo(TransactionType.PAYMENT));

        setResultConverter(button -> button == ButtonType.OK ? buildTransaction() : null);
    }

    private void validateBeforeClose(ActionEvent event) {
        try {
            buildTransaction();
        } catch (IllegalArgumentException ex) {
            showValidationError(ex.getMessage());
            event.consume();
        }
    }

    private Transaction buildTransaction() {
        String date = datePicker.getValue() != null ? datePicker.getValue().toString() : null;
        String description = descriptionField.getText().trim();
        if (date == null || description.isEmpty()) {
            throw new IllegalArgumentException("Datum und Beschreibung dürfen nicht leer sein.");
        }

        double amount;
        try {
            amount = Double.parseDouble(amountField.getText().trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Der Betrag muss eine Zahl sein.");
        }

        TransactionType type = typeChoice.getValue();
        if (type == TransactionType.PAYMENT) {
            return new Payment(date, amount, description,
                    bank.getIncomingInterest(), bank.getOutgoingInterest());
        }

        String sender = senderField.getText().trim();
        String recipient = recipientField.getText().trim();
        if (sender.isEmpty() || recipient.isEmpty()) {
            throw new IllegalArgumentException("Sender und Empfänger müssen angegeben werden.");
        }

        if (!sender.equals(accountName) && !recipient.equals(accountName)) {
            throw new IllegalArgumentException("Sender oder Empfänger muss dem aktuellen Account entsprechen.");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Der Betrag einer Überweisung muss positiv sein.");
        }

        return sender.equals(accountName)
                ? new OutgoingTransfer(date, amount, description, sender, recipient)
                : new IncomingTransfer(date, amount, description, sender, recipient);
    }

    private void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Eingabefehler");
        alert.setHeaderText("Bitte Eingaben prüfen");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
