package model;

import java.util.ArrayList;

public class CardStack {
    private String label;
    private ArrayList<Card> cards;

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

    public void setLabel(String label) {
        this.label = label;
    }

    // MODIFIES: this
    // EFFECTS: adds newCard to the card stack
    public void addCard(Card newCard) {
        cards.add(newCard);
    }

    // MODIFIES: ---
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
