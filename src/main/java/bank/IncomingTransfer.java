package bank;

/**
 * Repräsentiert eine eingehende Überweisung (IncomingTransfer) für ein Konto.
 * <p>
 * Ein IncomingTransfer ist eine Spezialisierung der Klasse {@link Transfer}.
 * Der berechnete Betrag ist immer positiv, da das Geld auf dem Zielkonto eingeht.
 */
public class IncomingTransfer extends Transfer {

    /**
     * Konstruktor zum Erstellen einer neuen eingehenden Überweisung.
     *
     * @param date        das Datum der Transaktion
     * @param amount      der Betrag der Transaktion (sollte positiv sein)
     * @param description die Beschreibung der Transaktion
     * @param sender      der Absender der Überweisung
     * @param recipient   der Empfänger der Überweisung
     */
    public IncomingTransfer(String date, double amount, String description,
                            String sender, String recipient) {
        super(date, amount, description, sender, recipient);
    }

    /**
     * Copy-Konstruktor zum Kopieren eines bestehenden Transfer-Objekts
     * als IncomingTransfer.
     *
     * @param t das zu kopierende Transfer-Objekt
     */
    public IncomingTransfer(Transfer t) {
        super(t);
    }


}
