package bank;

import bank.exceptions.*;

import java.io.IOException;
import java.util.List;

/**
 * Interface fuer eine generische Bank. Stellt mehrere Methoden bereit, um die Interaktion
 * zwischen Konten und Transaktionen zu verwalten.
 */
public interface Bank {

    /**
         * Fuegt der Bank ein neues Konto hinzu.
         *
         * @param account das hinzuzufuegende Konto
         * @throws AccountAlreadyExistException falls das Konto bereits existiert
     */
    void createAccount(String account) throws AccountAlreadyExistException,java.io.IOException;

    /**
         * Fuegt ein Konto mit einer vorgegebenen Transaktionsliste hinzu.
         * Wichtig: Doppelte Transaktionen duerfen dem Konto nicht erneut zugeordnet werden.
         *
         * @param account      das hinzuzufuegende Konto
         * @param transactions Liste bereits existierender Transaktionen, die dem neuen Konto hinzugefuegt werden sollen
         * @throws AccountAlreadyExistException     falls das Konto bereits existiert
         * @throws TransactionAlreadyExistException falls die Transaktion bereits existiert
         * @throws TransactionAttributeException    falls die Validierung der Attribute fehlschlaegt
     */
    void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistException, TransactionAlreadyExistException, TransactionAttributeException ,java.io.IOException;

    /**
        * Fuegt einem bestehenden Konto eine Transaktion hinzu.
         *
         * @param account     das Konto, dem die Transaktion hinzugefuegt wird
         * @param transaction die Transaktion, die gespeichert werden soll
         * @throws TransactionAlreadyExistException falls die Transaktion bereits existiert
         * @throws AccountDoesNotExistException     falls das Konto nicht existiert
         * @throws TransactionAttributeException    falls die Validierung der Attribute fehlschlaegt
     */
    void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException,java.io.IOException;

    /**
         * Entfernt eine Transaktion aus einem Konto. Falls sie nicht existiert, wird eine Exception
         * ausgeloest.
         *
         * @param account     das Konto, aus dem die Transaktion geloescht wird
         * @param transaction die zu entfernende Transaktion
         * @throws AccountDoesNotExistException     falls das Konto nicht existiert
         * @throws TransactionDoesNotExistException falls die Transaktion nicht gefunden wird
     */
    void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException,java.io.IOException;

        /**
                 * Loescht das angegebene Konto und entfernt jede persistierte Darstellung.
                 *
                 * @param account das Konto, das geloescht werden soll
                 * @throws AccountDoesNotExistException falls das Konto nicht gefunden wird
                 * @throws IOException                  falls die Kontodaten nicht aus dem Dateisystem entfernt werden koennen
         */
        void deleteAccount(String account) throws AccountDoesNotExistException, IOException;

    /**
         * Prueft, ob die angegebene Transaktion in dem Konto existiert.
         *
         * @param account     das Konto, in dem gesucht wird
         * @param transaction die gesuchte Transaktion
     */
    boolean containsTransaction(String account, Transaction transaction);

    /**
         * Berechnet und liefert den aktuellen Kontostand.
         *
         * @param account das ausgewaehlte Konto
         * @return aktueller Kontostand
     */
    double getAccountBalance(String account);

    /**
         * Liefert die Liste aller Transaktionen eines Kontos.
         *
         * @param account das ausgewaehlte Konto
         * @return Liste aller Transaktionen des Kontos
     */
    List<Transaction> getTransactions(String account);

    /**
         * Liefert eine sortierte Liste (nach berechneten Betraegen) fuer ein Konto. Sortiert wird
         * aufsteigend oder absteigend (oder leer, falls keine Daten vorhanden sind).
         *
         * @param account das ausgewaehlte Konto
         * @param asc     {@code true} fuer aufsteigend, {@code false} fuer absteigend
         * @return sortierte Liste aller Transaktionen des Kontos
     */
    List<Transaction> getTransactionsSorted(String account, boolean asc);

    /**
         * Liefert eine Liste der positiven oder negativen Transaktionen (berechnete Betraege).
         *
         * @param account  das ausgewaehlte Konto
         * @param positive {@code true} fuer Einnahmen, {@code false} fuer Ausgaben
         * @return Liste der Transaktionen nach Typ
     */
    List<Transaction> getTransactionsByType(String account, boolean positive);

        /**
                 * Gibt die Liste aller derzeit vorhandenen Kontonamen in der Bank zurueck.
                 *
                 * @return Liste der Konto-Bezeichner
         */
        List<String> getAllAccounts();
}
