package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a stack of index cards with a label for reference.
public class CardStack implements Writable {
    private String label;
    private ArrayList<Card> cards;

    // REQUIRES: label has a non-zero length
    // EFFECTS: Constructs a new empty CardStack with given label
    public CardStack(String label) {
        this.label = label;
        cards = new ArrayList<>();
    }

    public String getLabel() {
        return label;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    // MODIFIES: this
    // EFFECTS: adds newCard to the card stack
    public void addCard(Card newCard) {
        cards.add(newCard);
    }

    // EFFECTS: returns all the flagged cards in the card stack
    public ArrayList<Card> getFlagged() {
        ArrayList<Card> flaggedCards = new ArrayList<>();
        for (Card card : cards) {
            if (card.isFlagged()) {
                flaggedCards.add(card);
            }
        }
        return flaggedCards;
    }


    // EFFECTS: returns CardStack as a JSON Object with keys "label" and "cards",
    //          for each field, whose values are in String and JSONArray of cards respectively.
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("label", label);
        json.put("cards", cardsToJson());
        return json;
    }

    // EFFECTS: returns Cards in current CardStack as a JSONArray
    public JSONArray cardsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Card c : cards) {
            jsonArray.put(c.toJson());
        }
        return jsonArray;
    }
}
