package bank.exceptions;
/**
 * Wird geworfen, wenn ein Konto nicht existiert.
 */

public class AccountDoesNotExistException extends Exception {
    public AccountDoesNotExistException(String message) {
        super(message);
    }

}
