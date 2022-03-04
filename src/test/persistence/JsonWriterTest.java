package persistence;

import model.Card;
import model.CardStack;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //
    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //
    @Test
    void testWriterEmptyWorkroom() {
        try {
            ArrayList<CardStack> allStacks = new ArrayList<>();
            JsonWriter writer = new JsonWriter("./data/testWriterEmpty.json");
            writer.open();
            writer.write(allStacks);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmpty.json");
            allStacks = reader.read();
            assertTrue(allStacks.isEmpty());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }


    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //
    @Test
    void testWriterGeneralWorkroom() {
        try {
            ArrayList<CardStack> allStacks = new ArrayList<>();
            CardStack testStack1 = new CardStack("CPSC 210");
            testStack1.addCard(new Card ("Java","An object-orientated programming language"));
            testStack1.addCard(new Card ("UML","Diagram to describe program structure"));
            CardStack testStack2 = new CardStack("Notes on Supply-side Economics");
            testStack2.addCard(new Card ("M1","circulating money supply"));
            testStack2.addCard(new Card ("Inflation","Expansion of the money supply"));
            allStacks.add(testStack1);
            allStacks.add(testStack2);
            JsonWriter writer = new JsonWriter("./data/testWriterTwoStacks.json");
            writer.open();
            writer.write(allStacks);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterTwoStacks.json");
            allStacks = reader.read();
            assertEquals("CPSC 210",allStacks.get(0).getLabel());
            assertEquals("Notes on Supply-side Economics", allStacks.get(1).getLabel());
            assertEquals(2, allStacks.get(0).getCards().size());
            assertFalse(allStacks.get(0).getCards().get(0).isFlagged());
            assertEquals("Inflation", allStacks.get(1).getCards().get(1).getSideA());
            assertEquals("Expansion of the money supply", allStacks.get(1).getCards().get(1).getSideB());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}
