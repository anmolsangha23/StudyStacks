package ui;

import java.io.FileNotFoundException;

// main method to run StudyStacks program
public class Main {
    // EFFECTS: runs the main method for the project
    public static void main(String[] args) {
        try {
            new StudyStacksApp();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Closing application.");
        }
    }
}
