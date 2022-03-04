package persistence;

import model.CardStack;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

// Represents a writer for writing all current Card Stacks as JSON representation to file
public class JsonWriter {
    private static final int TAB = 4;

    private PrintWriter writer;
    private String target;

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //
    //EFFECTS: constructor for JsonWriter that writes to target filename
    public JsonWriter(String target) {
        this.target = target;
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //
    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if target file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(target));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON Array representation of ArrayList<CardStack> to file
    public void write(ArrayList<CardStack> allStacks) {
        JSONArray jsonArray = new JSONArray();
        for (CardStack cs : allStacks) {
            JSONObject json = cs.toJson();
            jsonArray.put(json);
        }
        saveToFile(jsonArray.toString(TAB));
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //
    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //
    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }

}
