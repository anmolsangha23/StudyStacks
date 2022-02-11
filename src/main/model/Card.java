package model;

// Represents an index card containing two sides of text and whether it is flagged for reference.
public class Card {
    private String sideA;
    private String sideB;
    private boolean flag;

    // EFFECTS: constructor for card with side A of card set to sideA; side B of card is set to sideB;
    //          flag is set to false (unflagged).
    public Card(String sideA, String sideB) {
        this.sideA = sideA;
        this.sideB = sideB;
        flag = false;
    }

    public String getSideA() {
        return sideA;
    }

    public String getSideB() {
        return sideB;
    }

    public boolean isFlagged() {
        return flag;
    }

    // MODIFIES: this
    // EFFECTS: card is flagged if unflagged; card is unflagged if flagged.
    public void flagUpdate() {
        flag = !flag;
    }
}
