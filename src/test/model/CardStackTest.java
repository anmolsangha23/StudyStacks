package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

// Tests for methods in CardStack class
public class CardStackTest {
    private CardStack testCardStack;
    private Card testCard1;
    private Card testCard2;
    private Card testCard3;

    @BeforeEach
    public void setUp() {
        testCardStack = new CardStack("CPSC 210");
        testCard1 = new Card("method",
                "a collection of statements that perform some specific task and return the result to the caller");
        testCard2 = new Card("inheritance",
                "the process where one class acquires the properties (methods and fields) of another");
        testCard3 = new Card("refactoring",
                "the process of changing existing code without changing its external behaviour");
    }

    @Test
    public void testConstructor() {
        assertEquals("CPSC 210", testCardStack.getLabel());
        assertTrue(testCardStack.getCards().isEmpty());
    }

    @Test
    public void testAddCard() {
        testCardStack.addCard(testCard1);
        assertEquals(Arrays.asList(testCard1),testCardStack.getCards());
        testCardStack.addCard(testCard2);
        assertEquals(Arrays.asList(testCard1,testCard2),testCardStack.getCards());
        testCardStack.addCard(testCard3);
        assertEquals(Arrays.asList(testCard1,testCard2,testCard3),testCardStack.getCards());
        testCardStack.addCard(testCard3);
        assertEquals(Arrays.asList(testCard1,testCard2,testCard3,testCard3),testCardStack.getCards());
    }

    @Test
    public void testGetFlagged() {
        assertTrue(testCardStack.getFlagged().isEmpty());
        testCardStack.addCard(testCard1);
        testCardStack.addCard(testCard2);
        testCardStack.addCard(testCard3);
        testCard1.flagUpdate();
        testCard3.flagUpdate();
        assertEquals(Arrays.asList(testCard1,testCard3),testCardStack.getFlagged());
    }

}
