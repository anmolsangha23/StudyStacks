package persistence;

// Represents a reader for parsing CardStack from JSON data stored in given file
public class JsonReader {
    private String filepath;

    // EFFECTS: constructs reader to read source file from given filepath
    public JsonReader(String filepath) {
        this.filepath = filepath;
    }

}
