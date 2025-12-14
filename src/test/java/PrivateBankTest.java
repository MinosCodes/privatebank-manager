import bank.*;
import bank.exceptions.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrivateBankTest {

    private static final String TEST_DIR = "test_data";
    private PrivateBank bank;

    @BeforeEach
    void init() throws Exception {
        Files.createDirectories(Paths.get(TEST_DIR));
        bank = new PrivateBank("TestBank", 0.05, 0.1, TEST_DIR);
    }

    @AfterEach
    void cleanup() throws IOException {
        Path dir = Paths.get(TEST_DIR);
        if (Files.exists(dir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path file : stream) {
                    Files.deleteIfExists(file);
                }
            }
        }
    }

    @Test
    void testConstructorAndDirectory() {
        assertEquals("TestBank", bank.getName());
        assertEquals(0.05, bank.getIncomingInterest());
        assertEquals(0.1, bank.getOutgoingInterest());
    }

    @Test
    void testCreateAccountAndPersistence() throws Exception {
        bank.createAccount("Adam");
        assertEquals(0.0, bank.getAccountBalance("Adam"), 0.0001);

        assertThrows(AccountAlreadyExistException.class,
                () -> bank.createAccount("Adam"));
    }

    @Test
    void testCreateAccountDoesNotThrowOnNewAccount() {
        assertDoesNotThrow(() -> bank.createAccount("Neu"));
    }

    @Test
    void testCreateAccountWithTransactions() throws Exception {
        Transaction t1 = new Payment("01.01.2025", 1000.0, "Lohn", 0.05, 0.1);
        Transaction t2 = new IncomingTransfer("02.01.2025", 200.0, "Geschenk", "Bob", "Eva");

        List<Transaction> list = List.of(t1, t2);

        bank.createAccount("Eva", list);

        assertEquals(2, bank.getTransactions("Eva").size());
        assertTrue(bank.containsTransaction("Eva", t1));
        assertTrue(bank.containsTransaction("Eva", t2));

        assertThrows(AccountAlreadyExistException.class,
                () -> bank.createAccount("Eva", list));
    }

    @Test
    void testCreateAccountWithIOException() {
        assertThrows(IOException.class, () ->
                new PrivateBank("BadBank", 0.05, 0.1, "///invalid///path"));
    }

    @Test
    void testDeleteAccountRemovesData() throws Exception {
        bank.createAccount("Adam");
        Path accountFile = Paths.get(TEST_DIR, "Konto_Adam.json");
        assertTrue(Files.exists(accountFile));

        bank.deleteAccount("Adam");

        assertFalse(Files.exists(accountFile));
        assertFalse(bank.getAllAccounts().contains("Adam"));
        assertThrows(AccountDoesNotExistException.class, () -> bank.deleteAccount("Adam"));
    }

    @Test
    void testGetAllAccountsSorted() throws Exception {
        bank.createAccount("Berta");
        bank.createAccount("Adam");

        List<String> accounts = bank.getAllAccounts();

        assertEquals(List.of("Adam", "Berta"), accounts);
    }

    @Test
    void testAddAndRemoveTransaction() throws Exception {
        bank.createAccount("Adam");

        Payment p = new Payment("01.01.2025", 1000.0, "Lohn", 0.05, 0.1);
        bank.addTransaction("Adam", p);

        assertTrue(bank.containsTransaction("Adam", p));
        assertFalse(bank.getTransactions("Adam").isEmpty());

        double balance = bank.getAccountBalance("Adam");
        assertNotEquals(0.0, balance);

        bank.removeTransaction("Adam", p);
        assertFalse(bank.containsTransaction("Adam", p));
    }

    @Test
    void testAddTransactionAccountDoesNotExist() {
        Payment p = new Payment("01.01.2025", 1000.0, "Lohn", 0.05, 0.1);
        assertThrows(AccountDoesNotExistException.class,
                () -> bank.addTransaction("Unbekannt", p));
    }

    @Test
    void testAddDuplicateTransaction() throws Exception {
        bank.createAccount("Adam");
        Payment p = new Payment("01.01.2025", 100.0, "Test", 0.05, 0.1);

        bank.addTransaction("Adam", p);
        assertThrows(TransactionAlreadyExistException.class,
                () -> bank.addTransaction("Adam", p));
    }

    @Test
    void testRemoveTransactionAccountDoesNotExist() {
        Payment p = new Payment("01.01.2025", 100.0, "Test", 0.05, 0.1);
        assertThrows(AccountDoesNotExistException.class,
                () -> bank.removeTransaction("Unbekannt", p));
    }

    @Test
    void testRemoveTransactionDoesNotExist() throws Exception {
        bank.createAccount("Adam");
        Payment p = new Payment("01.01.2025", 100.0, "Test", 0.05, 0.1);

        assertThrows(TransactionDoesNotExistException.class,
                () -> bank.removeTransaction("Adam", p));
    }

    @Test
    void testGetTransactionsSortedAndByType() throws Exception {
        bank.createAccount("Adam");

        Payment p1 = new Payment("01.01.2025", 1000.0, "Lohn", 0.05, 0.1);
        OutgoingTransfer out = new OutgoingTransfer("02.01.2025", 200.0, "Miete", "Adam", "Vermieter");

        bank.addTransaction("Adam", p1);
        bank.addTransaction("Adam", out);

        List<Transaction> asc = bank.getTransactionsSorted("Adam", true);
        List<Transaction> desc = bank.getTransactionsSorted("Adam", false);

        assertEquals(2, asc.size());
        assertEquals(2, desc.size());

        List<Transaction> positives = bank.getTransactionsByType("Adam", true);
        List<Transaction> negatives = bank.getTransactionsByType("Adam", false);

        assertFalse(positives.isEmpty());
        assertFalse(negatives.isEmpty());
    }

    // NEW: test copy constructor
    @Test
    void testCopyConstructor() throws Exception {
        bank.createAccount("Adam");
        Payment p = new Payment("01.01.2025", 1000.0, "Lohn", 0.05, 0.1);
        bank.addTransaction("Adam", p);

        PrivateBank copy = new PrivateBank(bank);

        assertEquals(bank.getName(), copy.getName());
        assertEquals(bank.getIncomingInterest(), copy.getIncomingInterest());
        assertEquals(bank.getOutgoingInterest(), copy.getOutgoingInterest());


        assertEquals(0.0, copy.getAccountBalance("Adam"), 0.0001);
    }

    @Test
    void testEqualsAndToString() throws Exception {
        PrivateBank b2 = new PrivateBank("TestBank", 0.05, 0.1, TEST_DIR);
        assertEquals(bank, b2);
        assertNotNull(bank.toString());
    }





}
