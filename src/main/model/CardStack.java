package model;

import java.util.ArrayList;

// Represents a stack of index cards with a label for reference.
public class CardStack {
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
}
