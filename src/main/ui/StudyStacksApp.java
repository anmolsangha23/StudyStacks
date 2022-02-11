package ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import model.*;

// StudyStacks Index Card Application
public class StudyStacksApp {
    private ArrayList<CardStack> allStacks;
    private Scanner input;

    // EFFECTS: initializes the StudyStacks application
    public StudyStacksApp() {
        allStacks = new ArrayList<>();
        input = new Scanner(System.in);
        runStudyStacks();
    }

    // MODIFIES: this
    // EFFECTS: runs the application starting from the main menu
    private void runStudyStacks() {
        boolean active = true;
        String command;

        while (active) {
            welcomeDialogue();
            command = input.nextLine();
            if (command.equals("e")) {
                active = false;
            } else if (command.equals("n")) {
                createNewStack();
            } else if (command.equals("v")) {
                stackMenu();
            } else {
                System.out.println("Unrecognized input. Please try again\n");
            }
        }
    }

    // EFFECTS: prints welcome menu with options to user
    private void welcomeDialogue() {
        System.out.println("Welcome to StudyStacks!");
        System.out.println("What would you like to do today? \nSelect one of the following:");
        System.out.println("\t> New Stack (n)");
        System.out.println("\t> View Stacks (v)");
        System.out.println("\t> Exit (e)");
    }

    // MODIFIES: this
    // EFFECTS: creates a new card stack with label from user input
    private void createNewStack() {
        System.out.println("Name of new stack:");
        CardStack newStack = new CardStack(input.nextLine());
        allStacks.add(newStack);
        selectedStackMenu(newStack);
    }

    // EFFECTS: prints list of current stacks with menu options and processes user input
    private void stackMenu() {
        String command;
        if (allStacks.isEmpty()) {
            noStacks();
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
                    return;
                }
            }
        }
    }

    // EFFECTS: prints menu options and processes user input for when no stacks present
    private void noStacks() {
        System.out.println("No current stacks. Create new stack?");
        System.out.println("\t> Yes (y)");
        System.out.println("\t> No, return to main menu (any other key)");
        String command = input.nextLine();
        if (command.equals("y")) {
            createNewStack();
        }
    }

    // EFFECTS: processes user input from printed options regarding currently selected stack
    private void selectedStackMenu(CardStack currentStack) {
        System.out.println(currentStack.getLabel() + " has " + currentStack.getCards().size() + " cards");
        selectedStackDialogue();
        String command = input.nextLine();
        if (command.equals("a")) {
            addCardToStack(currentStack);
        } else if (command.equals("v")) {
            viewCardList(currentStack.getCards());
        } else if (command.equals("f")) {
            viewFlaggedCards(currentStack);
        } else if (command.equals("r")) {
            viewRandomCards(currentStack);
        } else if (command.equals("d")) {
            deleteStack(currentStack);
        } else if (command.equals("e")) {
            stackMenu();
        } else {
            System.out.println("Unrecognized input. Returning to main menu\n");
        }
    }

    // EFFECTS: prints dialogue of options for selected stack to console
    private void selectedStackDialogue() {
        System.out.println("Select one of the following:");
        System.out.println("\t> Add new card to stack (a)");
        System.out.println("\t> View all cards in stack (v)");
        System.out.println("\t> View all currently flagged cards (f)");
        System.out.println("\t> Randomize and view stack (r)");
        System.out.println("\t> Delete stack (d)");
        System.out.println("\t> Exit to all stacks (e)");
    }

    // MODIFIES: this
    // EFFECTS: adds new card to current working stack with given user inputs
    private void addCardToStack(CardStack currentStack) {
        System.out.println("Side A of card:");
        String sideA = input.nextLine();
        System.out.println("Side B of card:");
        String sideB = input.nextLine();
        Card newCard = new Card(sideA,sideB);
        currentStack.addCard(newCard);
        selectedStackMenu(currentStack);
    }

    // MODIFIES: this
    // EFFECTS: deletes selected stack and all its cards; prints confirmation.
    private void deleteStack(CardStack currentStack) {
        allStacks.remove(currentStack);
        System.out.println("Stack successfully deleted. Returning to main menu\n");
    }

    // MODIFIES: this
    // EFFECTS: displays card in working stack with given user options to process input
    private void viewCardList(ArrayList<Card> currentCards) {
        int stackSize = currentCards.size();
        checkZero(stackSize);
        boolean isSideA = true;
        for (int i = 0; i < stackSize;) {
            currentCardDialogue(currentCards, stackSize, isSideA, i);
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
            } else {
                System.out.println("Unrecognized input. Please try again\n");
            }
        }
    }

    // EFFECTS: prints given statement if size is zero
    private void checkZero(int size) {
        if (size == 0) {
            System.out.println("No cards present. Returning to main menu\n");
        }
    }

    // EFFECTS: prints possible user inputs and information on current card in active stack
    private void currentCardDialogue(ArrayList<Card> currentCards, int stackSize, boolean isSideA, int i) {
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

    // EFFECTS: displays flagged cards in working stack with given user options to process input
    private void viewFlaggedCards(CardStack currentStack) {
        viewCardList(currentStack.getFlagged());
    }

    // EFFECTS: displays randomized card from working stack with given user options to process input
    private void viewRandomCards(CardStack currentStack) {
        ArrayList<Card> randomized = new ArrayList<>(currentStack.getCards());
        Collections.shuffle(randomized);
        viewCardList(randomized);
    }
}
