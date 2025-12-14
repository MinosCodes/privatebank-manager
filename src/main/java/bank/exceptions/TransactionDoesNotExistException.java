package bank.exceptions;
/**
 * Wird geworfen, wenn eine Transaktion im Konto nicht gefunden wird.
 */

public class TransactionDoesNotExistException extends Exception {
    public TransactionDoesNotExistException(String message) {
        super(message);
    }
}
