package bank;

/**
 * Basisklasse für finanzielle Transaktionen wie Überweisungen oder Ein- und Auszahlungen.
 *
 * @author MinosCodes
 */
public abstract class Transaction implements CalculateBill {

    /**
     * Datum der Transaktion.
     */
    protected String date;

    /**
     * Betrag der Transaktion.
     */
    protected double amount;

    /**
     * Beschreibung der Transaktion.
     */
    protected String description;

    /**
     * Konstruktor zur Initialisierung aller Attribute einer Transaktion.
     *
     * @param date Datum der Transaktion
     * @param amount Betrag der Transaktion
     * @param description Beschreibung der Transaktion
     */
    public Transaction(String date, double amount, String description) {
        this.date = date;
        setAmount(amount);
        this.description = description;
    }



    /**
     * @return Datum der Transaktion
     */
    public String getDate() {
        return this.date;
    }

    /**
     * @param date neues Datum für die Transaktion
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return Betrag der Transaktion
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * @param amount neuer Betrag für die Transaktion
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return Beschreibung der Transaktion
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description neue Beschreibung für die Transaktion
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * erzeugt eine zeichenkettendarstellung der transaction , die das Datum und die Beschreibung enthalt
     *
     * @return Eine String Date+description
     */
    @Override
    public String toString() {
        return "\ndate: " + date + "\ndescription: " + description + "Amount: " + this.calculate() + "\n" ;
    }

    /**
     * Vergleich dieses uberweisungsobjekt mit dem angegebenen Objekt auf gleichheit
     *
     * @param obj Das Objekt
     * @return true , wenn gleich False wenn nicht
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Transaction)) return false;
        if (obj == this) return true;
        Transaction other = (Transaction) obj;
        return date.equals(other.date)
                && amount == other.amount
                && description.equals(other.description);
    }
}
