package bank.exceptions;
/**
 * Wird geworfen, wenn ein Konto bereits existiert.
 */

public class AccountAlreadyExistException extends Exception {

    public AccountAlreadyExistException(String message) {
        super(message);
    }
}
