package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for methods in Card class
class CardTest {
    private Card testCard;

    @BeforeEach
    public void setUp() {
        testCard = new Card("method",
                "a collection of statements that perform some specific task and return the result to the caller");
    }

    @Test
    public void testConstructor() {
        assertEquals("method",testCard.getSideA());
        assertEquals("a collection of statements that perform some specific task " +
                        "and return the result to the caller",
                testCard.getSideB());
        assertFalse(testCard.isFlagged());
    }

    @Test
    public void testConstructorThreeParams() {
        Card testCardThree = new Card("exception","unexpected condition passed in program", true);
        assertEquals("exception",testCardThree.getSideA());
        assertEquals("unexpected condition passed in program",testCardThree.getSideB());
        assertTrue(testCardThree.isFlagged());
    }

    @Test
    public void testFlagUpdate() {
        testCard.flagUpdate();
        assertTrue(testCard.isFlagged());
        testCard.flagUpdate();
        assertFalse(testCard.isFlagged());
    }

    @Test
    public void testToJson() {
        JSONObject testJson = testCard.toJson();
        assertEquals("method",testJson.get("side_a"));
        assertEquals("a collection of statements that perform some specific task " +
                "and return the result to the caller", testJson.get("side_b"));
        assertFalse(testJson.getBoolean("flag"));
    }

    @Test
    public void testToJsonFlagged() {
        testCard.flagUpdate();
        JSONObject testJson = testCard.toJson();
        assertEquals("method",testJson.get("side_a"));
        assertEquals("a collection of statements that perform some specific task " +
                "and return the result to the caller", testJson.get("side_b"));
        assertTrue(testJson.getBoolean("flag"));
    }

}