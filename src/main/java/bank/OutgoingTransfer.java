package bank;

/**
 * Repräsentiert eine ausgehende Überweisung (OutgoingTransfer) von einem Konto.
 * <p>
 * Ein OutgoingTransfer ist eine Spezialisierung der Klasse {@link Transfer}.
 * Der berechnete Betrag ist immer negativ, da das Geld das Konto verlässt.
 */
public class OutgoingTransfer extends Transfer {

    /**
     * Konstruktor zum Erstellen einer neuen ausgehenden Überweisung.
     *
     * @param date        das Datum der Transaktion
     * @param amount      der Betrag der Transaktion (sollte positiv übergeben werden)
     * @param description die Beschreibung der Transaktion
     * @param sender      der Absender der Überweisung
     * @param recipient   der Empfänger der Überweisung
     */
    public OutgoingTransfer(String date, double amount, String description,
                            String sender, String recipient) {
        super(date, amount, description, sender, recipient);
    }

    /**
     * Berechnet den endgültigen Betrag der Transaktion.
     * <p>
     * Für ausgehende Überweisungen wird der Betrag stets negativ zurückgegeben.
     *
     * @return der negative Betrag der Überweisung
     */
    @Override
    public double calculate() {
        return -amount;
    }
}
