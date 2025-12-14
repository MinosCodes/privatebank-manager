package bank;

/**
 * Interface zur Berechnung des finalen Betrags einer Transaktion.
 *
 * @author MinosCodes
 */
public interface CalculateBill {

    /**
     * Berechnet den endgültigen Betrag abhängig von der Art der Transaktion.
     *
     * @return berechneter Betrag nach Zinsen oder Gebühren
     */
    double calculate();
}
