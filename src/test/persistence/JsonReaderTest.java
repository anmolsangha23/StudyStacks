package persistence;

import model.CardStack;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    @Test
    public void testReaderFileNotFound() {
        JsonReader testReader = new JsonReader("./data/imaginaryFile.json");
        try {
            ArrayList<CardStack> allStacks = testReader.read();
            fail();
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testReaderEmpty() {
        JsonReader testReader = new JsonReader("./data/testReaderEmpty.json");
        try {
            ArrayList<CardStack> allStacks = testReader.read();
            assertTrue(allStacks.isEmpty());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testReaderTwoCardStacks() {
        JsonReader testReader = new JsonReader("./data/testReaderTwoCardStacks.json");
        try {
            ArrayList<CardStack> allStacks = testReader.read();
            assertEquals("CPSC 210", allStacks.get(0).getLabel());
            assertEquals("MATH 200", allStacks.get(1).getLabel());
            assertEquals(3, allStacks.get(0).getCards().size());
            assertEquals("method", allStacks.get(0).getCards().get(0).getSideA());
            assertEquals("a collection of statements that perform some specific task " +
                            "and return the result to the caller",
                            allStacks.get(0).getCards().get(0).getSideB());
            assertFalse(allStacks.get(0).getCards().get(0).isFlagged());
            assertEquals("saddle point", allStacks.get(1).getCards().get(1).getSideA());
            assertEquals("critical point which is neither local max or local min",
                            allStacks.get(1).getCards().get(1).getSideB());
            assertTrue(allStacks.get(1).getCards().get(1).isFlagged());
        } catch (IOException e) {
            fail();
        }
    }
}
