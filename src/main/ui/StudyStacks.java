package ui;

import model.Card;
import model.CardStack;
import model.Event;
import model.EventLog;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static java.awt.Image.*;

// StudyStacks Index Card Application GUI Version
public class StudyStacks extends JFrame {
    JMenuBar menuBar;
    JMenu file;
    JMenuItem save;
    JMenuItem load;
    StackList stackList;
    JSplitPane splitPane;
    JSplitPane verticalSplitPane;
    JButton newStackButton;
    JButton deleteButton;
    JButton viewFlaggedCardsButton;
    JButton flipCardButton;
    JButton newCardButton;
    JButton randomizeButton;
    JButton nextCardButton;
    JButton previousCardButton;
    JButton flagCardButton;
    ImageIcon flagIcon;
    JList list;
    DefaultListModel listModel;
    CurrentCardPanel currentCardPanel;
    private ArrayList<CardStack> allStacks;
    private CardStack currentStack;
    private JsonWriter writer;
    private JsonReader reader;

    private static final String SAVED_STACKS_JSON = "./data/savedStacks.json";

    // EFFECTS: initializes the StudyStacks application
    public StudyStacks() {
        super("StudyStacks");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1250, 600));
        if (allStacks == null) {
            allStacks = new ArrayList<>();
        }
        writer = new JsonWriter(SAVED_STACKS_JSON);
        reader = new JsonReader(SAVED_STACKS_JSON);
        initMenu();
        JPanel buttonPanel = initButtons();
        verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        verticalSplitPane.setTopComponent(buttonPanel);
        currentCardPanel = new CurrentCardPanel(currentStack);
        verticalSplitPane.setBottomComponent(currentCardPanel);
        if (stackList == null) {
            stackList = new StackList();
        }
        initSplitPane();
        pack();
        setLocationRelativeTo(null);
        initEventLogListener();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes window listener for main frame that prints Event Log upon window closure.
    private void initEventLogListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (Event event : EventLog.getInstance()) {
                    System.out.println(event.toString() + "\n");
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: initializes main split pane in frame
    private void initSplitPane() {
        splitPane = new JSplitPane();
        splitPane.setLeftComponent(stackList);
        splitPane.setRightComponent(verticalSplitPane);
        splitPane.setDividerLocation(150);
        add(splitPane);
    }

    // MODIFIES: this
    // EFFECTS: initializes all buttons and adds listeners, returns buttonPanel
    private JPanel initButtons() {
        newStackButton = new JButton("New Stack");
        newStackButton.addActionListener(new NewStackListener());
        deleteButton = new JButton("Delete Stack");
        deleteButton.addActionListener(new DeleteListener());
        newCardButton = new JButton("New Card");
        newCardButton.addActionListener(new NewCardListener());
        randomizeButton = new JButton("Randomize!");
        randomizeButton.addActionListener(new RandomizeActionListener());
        nextCardButton = new JButton("Next Card");
        nextCardButton.addActionListener(new NextCardListener());
        previousCardButton = new JButton("Previous Card");
        previousCardButton.addActionListener(new PreviousCardListener());
        viewFlaggedCardsButton = new JButton("View Flagged Cards");
        viewFlaggedCardsButton.addActionListener(new ViewFlaggedCardsListener());
        flipCardButton = new JButton("Flip Card");
        flipCardButton.addActionListener(new FlipCardListener());
        initFlagIcon();
        flagCardButton = new JButton(flagIcon);
        flagCardButton.addActionListener(new FlagCardListener());
        JPanel buttonPanel = new JPanel();
        addButtons(buttonPanel);
        return buttonPanel;
    }

    // MODIFIES: this
    // EFFECTS: adds all buttons to buttonPanel
    private void addButtons(JPanel buttonPanel) {
        buttonPanel.add(newStackButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewFlaggedCardsButton);
        buttonPanel.add(randomizeButton);
        buttonPanel.add(newCardButton);
        buttonPanel.add(flagCardButton);
        buttonPanel.add(nextCardButton);
        buttonPanel.add(previousCardButton);
        buttonPanel.add(flipCardButton);
    }

    // MODIFIES: this
    // EFFECTS: initializes menu and adds listeners
    private void initMenu() {
        // create a menu component with save and load capabilities
        menuBar = new JMenuBar();
        file = new JMenu("File");
        save = new JMenuItem("Save");
        save.addActionListener(new SaveListener());
        load = new JMenuItem("Load");
        load.addActionListener(new LoadListener());
        menuBar.add(file);
        file.add(save);
        file.add(load);
        setJMenuBar(menuBar);
    }

    // Represents panel for displayed list of stacks
    public class StackList extends JPanel {
        JScrollPane listScrollPane;

        // EFFECTS: constructor for initialization of StackList with current allStacks
        public StackList() {
            listModel = new DefaultListModel();
            for (CardStack c : allStacks) {
                listModel.addElement(c.getLabel());
            }
            list = new JList(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.addListSelectionListener(new StackListListener());
            list.setVisibleRowCount(30);
            listScrollPane = new JScrollPane(list);
            listScrollPane.setPreferredSize(new Dimension(100,500));
            add(listScrollPane, BorderLayout.CENTER);
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes flagIcon from given image location
    public void initFlagIcon() {
        ImageIcon largeFlagIcon = new ImageIcon("./data/flagicon.jpg");
        Image flagImage = largeFlagIcon.getImage();
        Image newImg = flagImage.getScaledInstance(20, 20,  SCALE_SMOOTH);
        flagIcon = new ImageIcon(newImg);
    }

    // Represents listener for creating a new stack for newStackButton
    private class NewStackListener implements ActionListener {

        // MODIFIES: StudyStacks
        // EFFECTS: creates new stack with inputted name upon action event (button click)
        @Override
        public void actionPerformed(ActionEvent e) {
            String newLabel = JOptionPane.showInputDialog(list, "Name of new stack:",
                    "Create New Stack", JOptionPane.PLAIN_MESSAGE);
            if (newLabel != null) {
                allStacks.add(new CardStack(newLabel));
                listModel.addElement(newLabel);
                list.revalidate();
                list.repaint();
            }
        }
    }

    // Represents listener for newCardButton for creating a new card in current stack
    private class NewCardListener implements ActionListener {

        // MODIFIES: StudyStacks
        // EFFECTS: creates new card in current stack with inputted fields upon action event (button click)
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentStack != null) {
                String newSideA = JOptionPane.showInputDialog(list, "Side A:",
                        "Create New Card", JOptionPane.PLAIN_MESSAGE);
                String newSideB = JOptionPane.showInputDialog(list, "Side B:",
                        "Create New Card", JOptionPane.PLAIN_MESSAGE);
                if ((newSideA != null) && (newSideB != null)) {
                    Card newCard = new Card(newSideA,newSideB);
                    currentStack.addCard(newCard);
                    try {
                        currentCardPanel.remove(currentCardPanel.splashImage);
                    } catch (Exception notFirstException) {
                        // For catching exceptions when nothing is removed yet functionality remains.
                    }
                    currentCardPanel.displayCard();
                    currentCardPanel.revalidate();
                    currentCardPanel.repaint();
                }
            }
        }
    }

    // Represents listener for deleting selected stack for deleteButton
    private class DeleteListener implements ActionListener {

        // MODIFIES: StudyStacks
        // EFFECTS: deletes selected stack upon action event (button click)
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            if (index >= 0) {
                allStacks.remove(index);
                listModel.remove(index);
            }
            if (index == listModel.getSize()) {
                index--;
            }
            if (currentStack == null || !currentStack.getCards().isEmpty()) {
                currentCardPanel.remove(currentCardPanel.cardDisplay);
                currentCardPanel.add(currentCardPanel.splashImage);
            }
            list.revalidate();
            list.repaint();
            currentCardPanel.revalidate();
            currentCardPanel.repaint();
        }
    }

    // Represents panel for displaying current card contents or splash image
    private class CurrentCardPanel extends JPanel {
        CardStack currentCardStack;
        Card currentCard;
        Boolean isSideA = true;
        JLabel splashImage;
        int cardIndex = 0;
        JTextPane cardDisplay = new JTextPane();

        // EFFECTS: Constructor for CurrentCardPanel displaying card or graphic for given CardStack
        public CurrentCardPanel(CardStack cardStack) {
            cardDisplay.setEditable(false);
            cardDisplay.setBackground(Color.white);
            cardDisplay.setContentType("text/plain");
            initSplashImage();
            this.currentCardStack = cardStack;
            if (currentCardStack != null) {
                if (currentCardStack.getCards().isEmpty()) {
                    displayGraphic();
                } else {
                    displayCard();
                }
            } else {
                displayGraphic();
            }
        }

        // MODIFIES: this
        // EFFECTS: removes card display and displays splash image instead
        public void displayGraphic() {
            initSplashImage();
            remove(cardDisplay);
            this.revalidate();
            this.repaint();
            add(splashImage);
        }

        // MODIFIES: this
        // EFFECTS: initializes splashImage with given image location
        private void initSplashImage() {
            ImageIcon splashIcon = new ImageIcon("./data/studyStacksGraphic.jpg");
            Image flagImage = splashIcon.getImage();
            Image scaledSplashImage = flagImage.getScaledInstance(500, 300,SCALE_SMOOTH);
            splashIcon = new ImageIcon(scaledSplashImage);
            splashImage = new JLabel(splashIcon);
        }

        // MODIFIES: this
        // EFFECTS: displays current card in CurrentCardPanel
        public void displayCard() {
            if (currentCardStack != null) {
                currentCard = currentCardStack.getCards().get(cardIndex);
                if (isSideA) {
                    cardDisplay.setText(currentCard.getSideA());
                } else {
                    cardDisplay.setText(currentCard.getSideB());
                }
                add(cardDisplay);
                this.revalidate();
                this.repaint();
            }
        }

        // MODIFIES: this
        // EFFECTS: displays next card in stack in CurrentCardPanel
        public void nextCard() {
            if (!checkLast()) {
                cardIndex++;
                isSideA = true;
                displayCard();
            }
        }

        // MODIFIES: this
        // EFFECTS: displays previous card in stack in CurrentCardPanel
        public void previousCard() {
            if (!checkFirst()) {
                cardIndex--;
                isSideA = true;
                displayCard();
            }
        }

        // EFFECTS: checks if card is first card in stack
        public boolean checkFirst() {
            return cardIndex <= 0;
        }

        // EFFECTS: checks if card is last card in stack
        public boolean checkLast() {
            return ((cardIndex + 1) >= currentCardStack.getCards().size());
        }

        // MODIFIES: this
        // EFFECTS: displays other side of card in stack in CurrentCardPanel
        public void flipCard() {
            isSideA = !isSideA;
            displayCard();
        }
    }

    // Represents listener for nextCardButton for advancing to next card in current stack
    private class NextCardListener implements ActionListener {

        // MODIFIES: StudyStacks
        // EFFECTS: advances to next card in selected stack upon action event (button click)
        @Override
        public void actionPerformed(ActionEvent e) {
            if (list.getSelectedIndex() >= 0) {
                currentCardPanel.nextCard();
            }
        }
    }

    // Represents listener for previousCardButton for navigating to previous card in current stack
    private class PreviousCardListener implements ActionListener {

        // MODIFIES: StudyStacks
        // EFFECTS: navigates to previous card in selected stack upon action event (button click)
        @Override
        public void actionPerformed(ActionEvent e) {
            if (list.getSelectedIndex() >= 0) {
                currentCardPanel.previousCard();
            }
        }
    }

    // Represents listener for flagCardButton for flagging card in current stack
    private class FlagCardListener implements ActionListener {

        // MODIFIES: StudyStacks
        // EFFECTS: flags or unflags selected card in stack upon action event (button click)
        @Override
        public void actionPerformed(ActionEvent e) {
            if (list.getSelectedIndex() >= 0 && !currentStack.getCards().isEmpty()) {
                currentCardPanel.currentCard.flagUpdate();
                if (currentCardPanel.currentCard.isFlagged()) {
                    JOptionPane.showMessageDialog(flagCardButton, "Card flagged!");
                } else {
                    JOptionPane.showMessageDialog(flagCardButton, "Card unflagged!");
                }
            }
        }
    }

    // Represents listener for StackList for detecting changes in selected stack
    private class StackListListener implements ListSelectionListener {

        // MODIFIES: StudyStacks
        // EFFECTS: sets current card display panel to selected stack
        @Override
        public void valueChanged(ListSelectionEvent e) {
            int index = list.getSelectedIndex();
            try {
                currentStack = allStacks.get(index);
                currentCardPanel = new CurrentCardPanel(currentStack);
            } catch (Exception ee) {
                currentStack = null;
            }
            verticalSplitPane.setBottomComponent(currentCardPanel);
        }
    }

    // Represents listener for randomizeButton for displaying cards in current stack in a randomized order
    private class RandomizeActionListener implements ActionListener {

        // MODIFIES: StudyStacks
        // EFFECTS: Displays cards in current stack in a randomized order upon action event
        @Override
        public void actionPerformed(ActionEvent e) {
            if (list.getSelectedIndex() >= 0 && !currentStack.getCards().isEmpty()) {
                ArrayList<Card> randomized = new ArrayList<>(currentStack.getCards());
                Collections.shuffle(randomized);
                CardStack randomCards = new CardStack("random");
                for (Card c : randomized) {
                    randomCards.addCard(c);
                }
                try {
                    currentStack = randomCards;
                    currentCardPanel = new CurrentCardPanel(currentStack);
                } catch (Exception ee) {
                    currentStack = null;
                }
                verticalSplitPane.setBottomComponent(currentCardPanel);
            }
        }
    }

    // Represents listener for viewFlaggedCardsButton for displaying flagged cards in current stack
    private class ViewFlaggedCardsListener implements ActionListener {

        // MODIFIES: StudyStacks
        // EFFECTS: Displays flagged cards in current stack upon action event
        @Override
        public void actionPerformed(ActionEvent e) {
            if (list.getSelectedIndex() >= 0 && !currentStack.getFlagged().isEmpty()) {
                CardStack flaggedCards = new CardStack("flagged");
                for (Card c: currentStack.getFlagged()) {
                    flaggedCards.addCard(c);
                }
                try {
                    currentStack = flaggedCards;
                    currentCardPanel = new CurrentCardPanel(currentStack);
                } catch (Exception ee) {
                    currentStack = null;
                }
                verticalSplitPane.setBottomComponent(currentCardPanel);
            } else if (list.getSelectedIndex() >= 0 && !currentStack.getCards().isEmpty()) {
                JOptionPane.showMessageDialog(viewFlaggedCardsButton, "No flagged cards in stack!");
            }
        }
    }

    // Represents listener for flipCardButton for displaying other side of selected card in current stack
    private class FlipCardListener implements ActionListener {

        // MODIFIES: StudyStacks
        // EFFECTS: Displays other side of current card in selected stack in CurrentCardPanel
        @Override
        public void actionPerformed(ActionEvent e) {
            if (list.getSelectedIndex() >= 0 && !(currentStack.getCards().isEmpty())) {
                currentCardPanel.flipCard();
            }
        }
    }

    // Represents listener for save Menu Item for saving allStacks to json file
    private class SaveListener implements ActionListener {

        // MODIFIES: StudyStacks
        // EFFECTS: saves all card stacks to file upon action event
        @Override
        public void actionPerformed(ActionEvent e) {
            saveAllStacks();
        }
    }

    // MODIFIES: this
    // EFFECTS: saves all card stacks to file
    private void saveAllStacks() {
        try {
            writer.open();
            writer.write(allStacks);
            writer.close();
            revalidate();
            repaint();
        } catch (FileNotFoundException e) {
            System.out.println(SAVED_STACKS_JSON + " was unable to be opened to save card stacks");
        }
    }

    // Represents listener for load Menu Item for loading allStacks from json file
    private class LoadListener implements ActionListener {

        // MODIFIES: StudyStacks
        // EFFECTS: loads all card stacks from file upon action event
        @Override
        public void actionPerformed(ActionEvent e) {
            loadAllStacks();
        }
    }

    // MODIFIES: this
    // EFFECTS: loads all card stacks previously saved from file
    private void loadAllStacks() {
        try {
            allStacks = reader.read();
            splitPane.remove(stackList);
            stackList = new StackList();
            splitPane.setLeftComponent(stackList);
            revalidate();
            repaint();
        } catch (IOException e) {
            System.out.println("Given file " + SAVED_STACKS_JSON + "is not readable");
        }
    }
}
