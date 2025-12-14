package bank;

/**
 * Repräsentiert eine Ein- oder Auszahlung inklusive Zinsberechnung.
 *
 * @author MinosCodes
 */
public class Payment extends Transaction  {

    /**
     * Zinssatz bei einer Einzahlung (0 - 1).
     */
    private double incomingInterest;

    /**
     * Zinssatz bei einer Auszahlung (0 - 1).
     */
    private double outgoingInterest;

    /**
     * Konstruktor für ein Payment-Objekt.
     *
     * @param date Datum der Transaktion
     * @param amount Betrag der Transaktion (positiv = Einzahlung, negativ = Auszahlung)
     * @param description Beschreibung der Transaktion
     * @param incomingInterest Zinsen für Einzahlungen
     * @param outgoingInterest Zinsen für Auszahlungen
     */
    public Payment(String date, double amount, String description,
                   double incomingInterest, double outgoingInterest) {
        super(date, amount, description);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
    }

    /**
     * Kopierkonstruktor.
     *
     * @param payment zu kopierendes Payment
     */
    public Payment(Payment payment) {
        super(payment.getDate(), payment.getAmount(), payment.getDescription());
        this.incomingInterest = payment.getIncomingInterest();
        this.outgoingInterest = payment.getOutgoingInterest();
    }

    /**
     * @return incomingInterest Zinssatz für Einzahlungen
     */
    public double getIncomingInterest() {
        return incomingInterest;
    }

    /**
     * @param incomingInterest neuer Zinssatz für Einzahlungen (0-1)
     */
    public void setIncomingInterest(double incomingInterest) {
        if (incomingInterest >= 0 && incomingInterest <= 1)
            this.incomingInterest = incomingInterest;
        else
            System.out.println("Incoming interest ist fehlerhaft!");
    }

    /**
     * @return outgoingInterest Zinssatz für Auszahlungen
     */
    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    /**
     * @param outgoingInterest neuer Zinssatz für Auszahlungen (0-1)
     */
    public void setOutgoingInterest(double outgoingInterest) {
        if (outgoingInterest >= 0 && outgoingInterest <= 1)
            this.outgoingInterest = outgoingInterest;
        else {
            System.out.println("Outgoing interest ist fehlerhaft!");
            this.outgoingInterest = 0.0;
        }
    }

    /**
     * Berechnet den endgültigen Betrag nach Zinsen.
     *
     * @return finaler Betrag
     */
    @Override
    public double calculate() {
        if (this.getAmount() > 0) {
            return this.getAmount() * (1 + this.incomingInterest);
        } else {
            return this.getAmount() * (1 + this.outgoingInterest);
        }
    }
    /**
     * erzeugt eine zeichenkettendarstellung der transaction , die das Datum und die Beschreibung,outgoinginterest und incoöinginterest enthalt
     *
     * @return Eine String Date+description
     */

    @Override
    public String toString() {
        return "===== Payment Details =====\n" +

                super.toString() + "\n" +
                "OutgoingInterest: " + this.outgoingInterest + "\n" +
                "IncomingInterest: " + this.incomingInterest + "\n" +
                "===========================";
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
        if (!(obj instanceof Payment)) return false;
        Payment payment = (Payment) obj;
        return Double.compare(incomingInterest, payment.incomingInterest) == 0 &&
                Double.compare(outgoingInterest, payment.outgoingInterest) == 0 ;
    }
}

