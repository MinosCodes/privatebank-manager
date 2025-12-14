import bank.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {

    private Transfer t1;
    private Transfer tCopy;
    private IncomingTransfer in;
    private OutgoingTransfer out;

    @BeforeEach
    void init() {
        t1 = new Transfer("01.01.2025", 200.0, "Überweisung", "Alice", "Bob");
        tCopy = new Transfer(t1);
        in = new IncomingTransfer("02.01.2025", 300.0, "Eingang", "Carl", "Dana");
        out = new OutgoingTransfer("03.01.2025", 400.0, "Ausgang", "Eve", "Frank");
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("01.01.2025", t1.getDate());
        assertEquals(200.0, t1.getAmount());
        assertEquals("Überweisung", t1.getDescription());
        assertEquals("Alice", t1.getSender());
        assertEquals("Bob", t1.getRecipient());
    }

    @Test
    void testCopyConstructor() {
        assertEquals(t1, tCopy);
        assertNotSame(t1, tCopy);
    }

    @Test
    void testCalculateTransfer() {
        assertEquals(200.0, t1.calculate(), 0.0001);
    }

    @Test
    void testCalculateIncomingOutgoing() {
        assertEquals(300.0, in.calculate(), 0.0001);
        assertEquals(-400.0, out.calculate(), 0.0001);
    }

    @Test
    void testSetAmountNegativeSetsToZero() {
        Transfer t = new Transfer("01.01.2025", 100.0, "Test", "A", "B");
        t.setAmount(-50.0);
        assertEquals(0.0, t.getAmount());
    }

    @Test
    void testEqualsAndToString() {
        assertEquals(t1, tCopy);
        Transfer other = new Transfer("01.01.2025", 200.0, "Überweisung", "X", "Y");
        assertNotEquals(t1, other);
        assertNotNull(t1.toString());
    }
}
