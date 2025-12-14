package bank;

/**
 * Repräsentiert eine Überweisung zwischen einem Sender und einem Empfänger.
 *
 * @author MinosCodes
 */
public class Transfer extends Transaction {

    /**
     * Name des Senders.
     */
    private String sender;

    /**
     * Name des Empfängers.
     */
    private String recipient;

    /**
     * Konstruktor für ein Transfer-Objekt.
     *
     * @param date Datum der Überweisung
     * @param amount Betrag der Überweisung (muss positiv sein)
     * @param description Beschreibung der Überweisung
     * @param sender Sender der Überweisung
     * @param recipient Empfänger der Überweisung
     */
    public Transfer( String date,double amount, String description,
                    String sender, String recipient) {
        super(date, amount, description);
        this.sender = sender;
        this.recipient = recipient;

    }

    /**
     * Kopierkonstruktor.
     *
     * @param transfer zu kopierende Überweisung
     */
    public Transfer(Transfer transfer) {
        super(transfer.getDate(), transfer.getAmount(), transfer.getDescription());
        this.sender = transfer.sender;
        this.recipient = transfer.recipient;
    }

    /**
     * Setter mit Validierung: Betrag muss positiv sein.
     *
     * @param amount neuer Betrag
     */
    @Override
    public void setAmount(double amount) {
        if (amount > 0)
            this.amount = amount;
        else {
            System.out.println("Fehler: Betrag ist negativ!");
            this.amount = 0.0;
        }
    }
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    /**
     * Überweisungen haben keine Zinsen – es wird der reine Betrag zurückgegeben.
     *
     * @return Betrag ohne Veränderung
     */
    @Override
    public double calculate() {
        return this.getAmount();
    }

    /**
     * erzeugt eine zeichenkettendarstellung der transaction , die das Datum ,der Reciient ,der Sender und die Beschreibung enthalt
     *
     * @return Eine String Date+description
     */
    @Override
    public String toString() {
        return "===== Transfer Details =====\n" +

                super.toString() + "\n" +
                "Sender: " + this.sender + "\n" +
                "Recipient: " + this.recipient + "\n" +
                "============================";
    }
    /**
     * Vergleich dieses uberweisungsobjekt mit dem angegebenen Objekt auf gleichheit
     *
     * @param obj Das Objekt
     * @return true , wenn gleich False wenn nicht
     */

    @Override
    public boolean equals(Object obj) {
        if (!(super.equals(obj))) return false;
        if (!(obj instanceof Transfer)) return false;
        Transfer transfer = (Transfer) obj;
        return sender.equals(transfer.sender) &&
                recipient.equals(transfer.recipient);
    }
}
