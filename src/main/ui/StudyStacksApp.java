package ui;

import java.util.ArrayList;
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
            command = input.next();

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
        CardStack newStack = new CardStack(input.next());
        allStacks.add(newStack);
        selectedStackMenu(newStack);
    }

    private void stackMenu() {
        String command;
        if (allStacks.isEmpty()) {
            System.out.println("No current stacks. Create new stack? (y / n)");
            command = input.next();
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
            command = input.next();
            for (CardStack cs : allStacks) {
                if (command.equals(cs.getLabel())) {
                    selectedStackMenu(cs);
                }
            }
        }
    }

    private void selectedStackMenu(CardStack currentStack) {
        System.out.println(currentStack.getLabel() + " has " + currentStack.getCards().size() + " cards");
        System.out.println("Select one of the following:");
        System.out.println("\t> Add new card to stack (a)");
        System.out.println("\t> View all cards in stack (v)");
        System.out.println("\t> View all flagged cards (f)");
        System.out.println("\t> View all cards randomized (r)");
        System.out.println("\t> Exit to all stacks (e)");
        String command = input.next();
        if (command.equals("a")) {
            addCardToStack(currentStack);
        } else if (command.equals("v")) {
            viewAllCards(currentStack);
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
        String sideA = input.next();
        System.out.println("Side B of card:");
        String sideB = input.next();
        Card newCard = new Card(sideA,sideB);
        currentStack.addCard(newCard);
        selectedStackMenu(currentStack);
    }

    private void viewAllCards(CardStack currentStack) {
        int stackSize = currentStack.getCards().size();
        for (int i = 0; i < stackSize;) {
            System.out.println("-------------------");
            System.out.println(currentStack.getCards().get(i).getSideA());
            System.out.println("-------------------");
            System.out.println("enter d for next");
            String command = input.next();
            if (command.equals("d")) {
                i++;
            }
        }
    }

    private void viewRandomCards(CardStack currentStack) {
        // stub
    }

    private void viewFlaggedCards(CardStack currentStack) {
        // stub
    }

}
