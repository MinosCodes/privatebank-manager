package bank;

import bank.exceptions.*;
import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Die Klasse {@code PrivateBank} implementiert das Interface {@link Bank} und stellt
 * ein Banksystem dar, das mehrere Konten und deren zugehörige Transaktionen verwaltet.
 * <p>
 * Für jedes Konto wird eine Liste von Transaktionen gespeichert. Die Bank definiert die
 * globalen Zinssätze für Ein- und Auszahlungen, welche automatisch auf alle
 * {@link Payment}-Transaktionen angewendet werden.
 */
public class PrivateBank implements Bank {

    /** Name der Bank */
    private String name;

    /** Zinsrate für Einzahlungen (Wert zwischen 0 und 1) */
    private double incomingInterest;

    /** Zinsrate für Auszahlungen (Wert zwischen 0 und 1) */
    private double outgoingInterest;

    /** Zuordnung von Kontonamen zu Listen ihrer Transaktionen */
    private Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();

    private String directoryName;


    /**
     * Konstruktor für eine neue Bankinstanz.
     *
     * @param name Name der Bank
     * @param incomingInterest Zinsrate für Einzahlungen (0–1)
     * @param outgoingInterest Zinsrate für Auszahlungen (0–1)
     * @throws TransactionAttributeException wenn die Zinssätze ungültig sind
     * @throws IOException wenn directory nicht gefuncden ist
     */
    public PrivateBank(String name,
                        double incomingInterest,
                        double outgoingInterest,
                        String directoryName) throws TransactionAttributeException, IOException {

        setName(name);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
        this.directoryName = directoryName;

        // ensure directory exists
        Files.createDirectories(Paths.get(directoryName));

        // load existing accounts from JSON
        readAccounts();
    }

    /**
     * Copy-Konstruktor. Kopiert die ersten drei Attribute,
     * aber NICHT die accountsToTransactions-Map.
     *
     * @param other andere PrivateBank-Instanz
     */
    public PrivateBank(PrivateBank other) {
        this.name = other.name;
        this.incomingInterest = other.incomingInterest;
        this.outgoingInterest = other.outgoingInterest;
    }


    // -----------------------------------------------------
    // Getter und Setter
    // -----------------------------------------------------

    /** @return Name der Bank */
    public String getName() { return name; }

    /** @param name neuer Name der Bank */
    public void setName(String name) { this.name = name; }

    /** @return Zinsrate für Einzahlungen */
    public double getIncomingInterest() { return incomingInterest; }

    /**
     * Setzt den Zinssatz für Einzahlungen.
     *
     * @param incomingInterest Wert zwischen 0 und 1
     * @throws TransactionAttributeException wenn der Wert ungültig ist
     */
    public void setIncomingInterest(double incomingInterest) throws TransactionAttributeException {
        if (incomingInterest < 0 || incomingInterest > 1)
            throw new TransactionAttributeException("Incoming interest must be between 0 and 1");

        this.incomingInterest = incomingInterest;
    }

    /** @return Zinsrate für Auszahlungen */
    public double getOutgoingInterest() { return outgoingInterest; }

    /**
     * Setzt den Zinssatz für Auszahlungen.
     *
     * @param outgoingInterest Wert zwischen 0 und 1
     * @throws TransactionAttributeException wenn der Wert ungültig ist
     */
    public void setOutgoingInterest(double outgoingInterest) throws TransactionAttributeException {
        if (outgoingInterest < 0 || outgoingInterest > 1)
            throw new TransactionAttributeException("Outgoing interest must be between 0 and 1");

        this.outgoingInterest = outgoingInterest;
    }


    // -----------------------------------------------------
    // toString & equals
    // -----------------------------------------------------

    /**
     * Gibt eine Textdarstellung der Bank aus.
     *
     * @return String-Repräsentation der PrivateBank
     */
    @Override
    public String toString() {
        return "PrivateBank {" +
                "name='" + name + '\'' +
                ", incomingInterest=" + incomingInterest +
                ", outgoingInterest=" + outgoingInterest +
                ", accountsToTransactions=" + accountsToTransactions.keySet() +
                '}';
    }

    /**
     * Vergleicht alle Attribute zweier PrivateBank-Objekte.
     *
     * @param obj anderes Objekt
     * @return {@code true}, wenn alle Attribute übereinstimmen
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        PrivateBank other = (PrivateBank) obj;

        return Objects.equals(this.name, other.name)
                && this.incomingInterest == other.incomingInterest
                && this.outgoingInterest == other.outgoingInterest
                && Objects.equals(this.accountsToTransactions, other.accountsToTransactions);
    }


    // -----------------------------------------------------
    // Kontoerstellung
    // -----------------------------------------------------

    /**
     * Erstellt ein neues Konto.
     *
     * @param account Kontoname
     * @throws AccountAlreadyExistException wenn das Konto bereits existiert
     * @throws IOException wenn das Konto nicht im Dateisystem gespeichert werden kann
     */
    @Override
    public void createAccount(String account) throws AccountAlreadyExistException , java.io.IOException{
        if (accountsToTransactions.containsKey(account))
            throw new AccountAlreadyExistException("Account already exists: " + account);

        accountsToTransactions.put(account, new ArrayList<>());
        writeAccount(account);
    }

    /**
     * Erstellt ein Konto und fügt eine Liste von Transaktionen hinzu.
     *
     * @param account Kontoname
     * @param transactions Liste von Transaktionen
     * @throws AccountAlreadyExistException wenn das Konto bereits existiert
     * @throws TransactionAlreadyExistException bei doppelten Transaktionen
     * @throws TransactionAttributeException wenn Attribute ungültig sind
     * @throws IOException wenn das Konto nicht im Dateisystem gespeichert werden kann
     */
    public void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistException, TransactionAttributeException, TransactionAlreadyExistException , IOException{

        createAccount(account);

        for (Transaction transaction : transactions) {
            try {
                addTransaction(account, transaction);
            } catch (AccountDoesNotExistException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    // -----------------------------------------------------
    // Transaktionen hinzufügen
    // -----------------------------------------------------

    /**
     * Fügt einer Konto eine Transaktion hinzu.
     *
     * @param account Kontoname
     * @param transaction hinzuzufügende Transaktion
     * @throws TransactionAlreadyExistException bei doppelten Transaktionen
     * @throws AccountDoesNotExistException wenn das Konto nicht existiert
     * @throws TransactionAttributeException bei ungültigen Attributen
     * @throws IOException wenn das Konto nicht im Dateisystem gespeichert werden kann
     */
    @Override
    public void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException,IOException{

        List<Transaction> list = accountsToTransactions.get(account);
        if (list == null)
            throw new AccountDoesNotExistException("Konto nicht gefunden: " + account);

        if (transaction instanceof Payment p) {
            p.setIncomingInterest(this.incomingInterest);
            p.setOutgoingInterest(this.outgoingInterest);
        }

        if (containsTransaction(account, transaction))
            throw new TransactionAlreadyExistException("Transaktion existiert bereits im Konto " + account);

        list.add(transaction);
        writeAccount(account);
    }


    // -----------------------------------------------------
    // Transaktion entfernen
    // -----------------------------------------------------

    /**
     * Entfernt eine Transaktion aus einem Konto.
     *
     * @param account Konto
     * @param transaction zu löschende Transaktion
     * @throws AccountDoesNotExistException wenn Konto nicht existiert
     * @throws TransactionDoesNotExistException wenn Transaktion nicht existiert
     * @throws IOException wenn das Konto nicht im Dateisystem gespeichert werden kann
     */
    @Override
    public void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException ,IOException {

        if (!accountsToTransactions.containsKey(account))
            throw new AccountDoesNotExistException("Account does not exist.");

        if (!accountsToTransactions.get(account).remove(transaction))
            throw new TransactionDoesNotExistException("Transaction does not exist.");
        writeAccount(account);
    }

    // -----------------------------------------------------
    // Konto löschen & Übersicht
    // -----------------------------------------------------

    /** {@inheritDoc} */
    @Override
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Account does not exist: " + account);
        }

        accountsToTransactions.remove(account);
        Path file = Paths.get(directoryName, "Konto_" + account + ".json");
        Files.deleteIfExists(file);
    }


    // -----------------------------------------------------
    // Überprüfen und Abfragen
    // -----------------------------------------------------

    /** {@inheritDoc} */
    @Override
    public boolean containsTransaction(String account, Transaction transaction) {
        if (!accountsToTransactions.containsKey(account)) return false;
        return accountsToTransactions.get(account).contains(transaction);
    }

    /** {@inheritDoc} */
    @Override
    public double getAccountBalance(String account) {
        if (!accountsToTransactions.containsKey(account)) return 0;

        double balance = 0;
        for (Transaction t : accountsToTransactions.get(account))
            balance += t.calculate();

        return balance;
    }

    /** {@inheritDoc} */
    @Override
    public List<Transaction> getTransactions(String account) {
        return new ArrayList<>(accountsToTransactions.get(account));
    }

    /** {@inheritDoc} */
    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {
        List<Transaction> sorted = new ArrayList<>(accountsToTransactions.get(account));

        sorted.sort((a, b) ->
                asc ? Double.compare(a.calculate(), b.calculate())
                        : Double.compare(b.calculate(), a.calculate()));

        return sorted;
    }

    /** {@inheritDoc} */
    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) {
        List<Transaction> result = new ArrayList<>();

        for (Transaction t : accountsToTransactions.get(account)) {
            double value = t.calculate();
            if (positive && value >= 0) result.add(t);
            if (!positive && value < 0) result.add(t);
        }

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public List<String> getAllAccounts() {
        List<String> accounts = new ArrayList<>(accountsToTransactions.keySet());
        Collections.sort(accounts);
        return accounts;
    }
    /**
     * Erzeugt eine konfigurierte Gson-Instanz zur (De-)Serialisierung
     * von {@link Transaction}-Objekten mit dem registrierten
     * {@link TransactionSerDer} und formatiertem JSON-Output.
     *
     * @return die konfigurierte Gson-Instanz
     */
    private Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Transaction.class, new TransactionSerDer())
                .setPrettyPrinting()
                .create();
    }
    /**
     * Liest alle im Verzeichnis {@code directoryName} vorhandenen Kontodateien
     * (JSON) ein und baut daraus die interne Zuordnung von Kontonamen zu
     * Transaktionslisten ({@code accountsToTransactions}) auf.
     * <p>
     * Es werden nur Dateien berücksichtigt, deren Name mit {@code "Konto_"}
     * beginnt und auf {@code ".json"} endet.
     *
     * @throws IOException wenn beim Zugriff auf das Dateisystem ein Fehler auftritt
     */

    private void readAccounts() throws IOException {
        accountsToTransactions.clear();

        Path dir = Paths.get(directoryName);
        if (!Files.exists(dir)) {
            return; // nothing to read
        }

        Gson gson = createGson();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.json")) {
            for (Path file : stream) {
                String fileName = file.getFileName().toString();      // beispiel "Konto_Adam.json"

                // nur Konto_// .json
                if (!fileName.startsWith("Konto_") || !fileName.endsWith(".json")) {
                    continue;
                }

                // extract name
                String accountName = fileName.substring(
                        "Konto_".length(),
                        fileName.length() - ".json".length()
                );

                String json = Files.readString(file);
                Transaction[] arr = gson.fromJson(json, Transaction[].class);

                List<Transaction> list = new ArrayList<>();
                if (arr != null) {
                    list.addAll(Arrays.asList(arr));
                }
                accountsToTransactions.put(accountName, list);
            }
        }
    }

    /**
     * Persistiert alle Transaktionen des angegebenen Kontos in einer JSON-Datei
     * im Verzeichnis {@code directoryName}. Der Dateiname folgt dem Schema
     * {@code "Konto_<account>.json"}.
     *
     * @param account Name des Kontos, dessen Transaktionen gespeichert werden sollen
     * @throws IOException wenn die Kontodaten nicht in das Dateisystem geschrieben werden können
     */

    private void writeAccount(String account) throws IOException {
        List<Transaction> list = accountsToTransactions.get(account);
        if (list == null) return;//

        Gson gson = createGson();

        String json = gson.toJson(list.toArray(new Transaction[0]), Transaction[].class);

        String fileName = "Konto_" + account + ".json";
        Path file = Paths.get(directoryName, fileName);
        Files.writeString(file, json);
    }



}
