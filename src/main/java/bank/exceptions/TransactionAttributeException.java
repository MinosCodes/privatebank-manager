package bank.exceptions;
/**
 * Wird geworfen, wenn Attribute einer Transaktion unzulässige Werte haben
 */

public class TransactionAttributeException extends Exception {
    public TransactionAttributeException(String message) {
        super(message);
    }


}
