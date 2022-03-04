package persistence;

import model.Card;
import model.CardStack;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

// Represents a reader for reading all Card Stacks from JSON data stored in given file
public class JsonReader {
    private String filepath;

    // EFFECTS: constructs reader to read source file from given filepath
    public JsonReader(String filepath) {
        this.filepath = filepath;
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //
    // EFFECTS: reads and returns ArrayList<CardStack> from given file;
    // throws IOException if an error occurs reading data
    public ArrayList<CardStack> read() throws IOException {
        String jsonData = readFile(filepath);
        JSONArray jsonArray = new JSONArray(jsonData);
        return parseListCardStack(jsonArray);
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //
    // EFFECTS: reads source file from its filepath and returns it as String
    private String readFile(String filepath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filepath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses ArrayList<CardStack> from JSON Object and returns it
    private ArrayList<CardStack> parseListCardStack(JSONArray jsonArray) {
        ArrayList<CardStack> allStacks = new ArrayList<>();
        for (Object cs : jsonArray) {
            CardStack nextStack = parseCardStack((JSONObject) cs);
            allStacks.add(nextStack);
        }
        return allStacks;
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //
    // EFFECTS: parses CardStack from JSON object and returns it
    private CardStack parseCardStack(JSONObject jsonObject) {
        String label = jsonObject.getString("label");
        CardStack myCardStack = new CardStack(label);
        addCards(myCardStack, jsonObject);
        return myCardStack;
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //
    // MODIFIES: cs
    // EFFECTS: parses cards from JSON object and adds them to CardStack
    private void addCards(CardStack cs, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("cards");
        for (Object json : jsonArray) {
            JSONObject nextCard = (JSONObject) json;
            addCard(cs, nextCard);
        }
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //
    // MODIFIES: cs
    // EFFECTS: parses card from JSON object and adds it to cs
    private void addCard(CardStack cs, JSONObject jsonObject) {
        String sideA = jsonObject.getString("side_a");
        String sideB = jsonObject.getString("side_b");
        boolean flag = jsonObject.getBoolean("flag");
        Card foundCard = new Card(sideA, sideB, flag);
        cs.addCard(foundCard);
    }

}
