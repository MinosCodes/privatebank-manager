import bank.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainJsonTest {
    public static void main(String[] args) throws Exception {
        String dir = "data_json_test";

        // 1. Erste Bank  schreibt JSON
        PrivateBank bank1 = new PrivateBank("JsonBank", 0.05, 0.1, dir);

        try {
            bank1.createAccount("Adam");
        } catch (bank.exceptions.AccountAlreadyExistException e) {
            System.out.println("Konto Adam existiert schon.");
        }

        Payment p = new Payment("01.01.2025", 1000.0, "Lohn", 0.05, 0.1);
        IncomingTransfer in = new IncomingTransfer(
                "02.01.2025", 200.0, "Geschenk", "Bob", "Adam");

        if (!bank1.containsTransaction("Adam", p)) {
            bank1.addTransaction("Adam", p);
        }
        if (!bank1.containsTransaction("Adam", in)) {
            bank1.addTransaction("Adam", in);
        }

        System.out.println("=== Bank1: Kontostand und Transaktionen ===");
        System.out.println("Balance Adam: " + bank1.getAccountBalance("Adam"));
        for (Transaction t : bank1.getTransactions("Adam")) {
            System.out.println(t);
        }

        // 2. JSON-Datei anzeigen
        Path jsonFile = Paths.get(dir, "Konto_Adam.json");
        if (Files.exists(jsonFile)) {
            System.out.println("\n=== Inhalt von Konto_Adam.json ===");
            String json = Files.readString(jsonFile);
            System.out.println(json);
        } else {
            System.out.println("JSON-Datei nicht gefunden: " + jsonFile);
        }

        // 3. Zweite Bankinstanz  liest aus JSON
        PrivateBank bank2 = new PrivateBank("JsonBank", 0.05, 0.1, dir);

        System.out.println("\n=== Bank2 (nach Deserialisierung) ===");
        System.out.println("Balance Adam: " + bank2.getAccountBalance("Adam"));
        for (Transaction t : bank2.getTransactions("Adam")) {
            System.out.println(t);
        }
    }
}
