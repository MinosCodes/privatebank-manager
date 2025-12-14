package bank.exceptions;
/**
 * Wird geworfen, wenn eine Transaktion bereits im Konto existiert.
 */

public class TransactionAlreadyExistException extends Exception {
    public TransactionAlreadyExistException(String message) {
        super(message);
    }

}
