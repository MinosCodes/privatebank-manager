import bank.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Payment p1;
    private Payment pCopy;

    @BeforeEach
    void init() {

        p1 = new Payment("01.01.2025", 1000.0, "Lohn", 0.05, 0.1);
        pCopy = new Payment(p1);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("01.01.2025", p1.getDate());
        assertEquals(1000.0, p1.getAmount());
        assertEquals("Lohn", p1.getDescription());
        assertEquals(0.05, p1.getIncomingInterest());
        assertEquals(0.1, p1.getOutgoingInterest());
    }

    @Test
    void testCopyConstructor() {
        assertEquals(p1, pCopy);
        assertNotSame(p1, pCopy);
    }

    @Test
    void testCalculate() {
        double result = p1.calculate();
        assertEquals(1050.0, result, 0.0001);
    }

    @Test
    void testEqualsAndToString() {
        assertEquals(p1, pCopy);
        Payment other = new Payment("02.01.2025", 500.0, "Bonus", 0.05, 0.1);
        assertNotEquals(p1, other);
        assertNotNull(p1.toString());
    }

    @ParameterizedTest
    @ValueSource(doubles = {200.0, 300.0, 1000.0,425.0})
    void testincomingtest(double Amount) {

        double expected = Amount * (1 + 0.05);

        Payment payment = new Payment("01.01.2025", Amount, "Incoming Test", 0.05, 0.1);

        assertEquals(expected, payment.calculate(), 0.001,
                "Incoming payment must be increased negatively by the incomingInterest.");
    }

}


