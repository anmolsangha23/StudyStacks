package persistence;

import org.json.JSONObject;

// represents interface for class that is writable as a json object
public interface Writable {

    // EFFECTS: returns this object as a JSONObject
    JSONObject toJson();
}
