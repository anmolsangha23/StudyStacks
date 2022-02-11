package ui;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import model.*;

public class StudyStacksApp {
    private ArrayList<CardStack> allStacks;
    private Scanner input;

    // EFFECTS: runs the StudyStacks application
    public StudyStacksApp() {
        allStacks = new ArrayList<>();
        input = new Scanner(System.in);
        runStudyStacks();
    }

    // MODIFIES: this
    // EFFECTS: initializes service
    private void runStudyStacks() {
        boolean active = true;
        String command;

        while (active) {
            welcomeDialog();
            command = input.nextLine();

            if (command.equals("e")) {
                active = false;
            } else if (command.equals("n")) {
                createNewStack();
            } else if (command.equals("v")) {
                stackMenu();
            }
        }
    }

    private void welcomeDialog() {
        System.out.println("Welcome to StudyStacks!");
        System.out.println("What would you like to do today? \nSelect one of the following:");
        System.out.println("\t> New Stack (n)");
        System.out.println("\t> View Stacks (v)");
        System.out.println("\t> Exit (e)");
    }

    // MODIFIES: this
    private void createNewStack() {
        System.out.println("Name of new stack:");
        CardStack newStack = new CardStack(input.nextLine());
        allStacks.add(newStack);
        selectedStackMenu(newStack);
    }

    private void stackMenu() {
        String command;
        if (allStacks.isEmpty()) {
            System.out.println("No current stacks. Create new stack? (y / n)");
            command = input.nextLine();
            if (command.equals("y")) {
                createNewStack();
            } else if (command.equals("n")) {
                // welcomeDialog();
            }
        } else {
            System.out.println("Your current stacks:");
            for (CardStack cs : allStacks) {
                System.out.println("> " + cs.getLabel());
            }
            System.out.println("Type in name of stack to access or any other input to return to main menu:");
            command = input.nextLine();
            for (CardStack cs : allStacks) {
                if (command.equals(cs.getLabel())) {
                    selectedStackMenu(cs);
                } else {
                    runStudyStacks();
                }
            }
        }
    }

    private void selectedStackMenu(CardStack currentStack) {
        System.out.println(currentStack.getLabel() + " has " + currentStack.getCards().size() + " cards");
        System.out.println("Select one of the following:");
        System.out.println("\t> Add new card to stack (a)");
        System.out.println("\t> View all cards in stack (v)");
        System.out.println("\t> View all currently flagged cards (f)");
        System.out.println("\t> Randomize and view stack (r)");
        System.out.println("\t> Exit to all stacks (e)");
        String command = input.nextLine();
        if (command.equals("a")) {
            addCardToStack(currentStack);
        } else if (command.equals("v")) {
            viewCardList(currentStack.getCards());
        } else if (command.equals("f")) {
            viewFlaggedCards(currentStack);
        } else if (command.equals("r")) {
            viewRandomCards(currentStack);
        } else if (command.equals("e")) {
            stackMenu();
        }
    }

    private void addCardToStack(CardStack currentStack) {
        System.out.println("Side A of card:");
        String sideA = input.nextLine();
        System.out.println("Side B of card:");
        String sideB = input.nextLine();
        Card newCard = new Card(sideA,sideB);
        currentStack.addCard(newCard);
        selectedStackMenu(currentStack);
    }

    private void viewCardList(ArrayList<Card> currentCards) {
        int stackSize = currentCards.size();
        boolean isSideA = true;
        for (int i = 0; i < stackSize;) {
            cardListOptions(currentCards, stackSize, isSideA, i);
            String command = input.nextLine();
            if (command.equals("c")) {
                isSideA = !isSideA;
            } else if (command.equals("n") && i != (stackSize - 1)) {
                isSideA = true;
                i++;
            } else if (command.equals("p") && i != 0) {
                isSideA = true;
                i--;
            } else if (command.equals("f")) {
                currentCards.get(i).flagUpdate();
            } else if (command.equals("e")) {
                break;
            }
        }
    }

    private void cardListOptions(ArrayList<Card> currentCards, int stackSize, boolean isSideA, int i) {
        System.out.println("-----------------------------");
        if (isSideA) {
            System.out.println(currentCards.get(i).getSideA());
        } else {
            System.out.println(currentCards.get(i).getSideB());
        }
        System.out.println("-----------------------------");
        System.out.println("Select one of the following:");
        System.out.println("\t> See other side of card (c)");
        if (currentCards.get(i).isFlagged()) {
            System.out.println("\t> Unflag card (f)");
        } else {
            System.out.println("\t> Flag card (f)");
        }
        if (i != (stackSize - 1)) {
            System.out.println("\t> Next card (n)");
        }
        if (i != 0) {
            System.out.println("\t> Previous card (p)");
        }
        System.out.println("\t> Exit to main menu (e)");
    }

    private void viewFlaggedCards(CardStack currentStack) {
        viewCardList(currentStack.getFlagged());
    }

    private void viewRandomCards(CardStack currentStack) {
        Random random = new Random();

    }

}
