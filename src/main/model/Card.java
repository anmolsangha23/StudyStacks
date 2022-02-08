package model;

public class Card {
    private String sideA;
    private String sideB;
    private boolean flag;

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

    public void setSideA(String input) {
        this.sideA = input;
    }

    public void setSideB(String input) {
        this.sideB = input;
    }

    // MODIFIES: this
    // EFFECTS: card is flagged if unflagged; card is unflagged if flagged
    public void flagUpdate() {
        flag = !flag;
    }


}
