package ui;

import model.Card;
import model.CardStack;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static java.awt.Image.*;

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
    private ArrayList<CardStack> allStacks;
    private CardStack currentStack;
    private JsonWriter writer;
    private JsonReader reader;

    CurrentCardPanel currentCardPanel;

    private static final String SAVED_STACKS_JSON = "./data/savedStacks.json";

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

        // add next functions to frame

        JPanel buttonPanel = initButtons();

        verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        verticalSplitPane.setTopComponent(buttonPanel);
        currentCardPanel = new CurrentCardPanel(currentStack);
        verticalSplitPane.setBottomComponent(currentCardPanel);

        splitPane = new JSplitPane();
        if (stackList == null) {
            stackList = new StackList();
        }
        splitPane.setLeftComponent(stackList);
        splitPane.setRightComponent(verticalSplitPane);
        splitPane.setDividerLocation(150);


        add(splitPane);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

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

    public static void main(String[] args) {
        StudyStacks studyStacks = new StudyStacks();
    }

    public class StackList extends JPanel {
        JScrollPane listScrollPane;

        public StackList() {
            listModel = new DefaultListModel();
            for (CardStack c : allStacks) {
                listModel.addElement(c.getLabel());
            }
            list = new JList(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.addListSelectionListener(new CardCreatorListener());
            list.setVisibleRowCount(30);
            listScrollPane = new JScrollPane(list);
            listScrollPane.setPreferredSize(new Dimension(100,500));
            add(listScrollPane, BorderLayout.CENTER);
        }
    }

    public void initFlagIcon() {
        ImageIcon largeFlagIcon = new ImageIcon("./data/flagicon.jpg");
        Image flagImage = largeFlagIcon.getImage();
        Image newImg = flagImage.getScaledInstance(20, 20,  SCALE_SMOOTH);
        flagIcon = new ImageIcon(newImg);
    }

    private class DeleteListener implements ActionListener {
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
            list.revalidate();
            list.repaint();
            currentCardPanel.revalidate();
            currentCardPanel.repaint();
        }
    }

    private class NewStackListener implements ActionListener {
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

    private class NewCardListener implements ActionListener {
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

    private class CurrentCardPanel extends JPanel {
        CardStack currentCardStack;
        Card currentCard;
        Boolean isSideA = true;
        JLabel splashImage;
        int cardIndex = 0;
        JTextPane cardDisplay = new JTextPane();

        public CurrentCardPanel(CardStack cardStack) {
            cardDisplay.setEditable(false);
            cardDisplay.setBackground(Color.white);
            cardDisplay.setContentType("text/plain");
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

        public void displayGraphic() {
            ImageIcon splashIcon = new ImageIcon("./data/studyStacksGraphic.jpg");
            Image flagImage = splashIcon.getImage();
            Image scaledSplashImage = flagImage.getScaledInstance(500, 300,SCALE_SMOOTH);
            splashIcon = new ImageIcon(scaledSplashImage);
            splashImage = new JLabel(splashIcon);
            remove(cardDisplay);
            this.revalidate();
            this.repaint();
            add(splashImage);
        }

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

        public void nextCard() {
            if (!checkLast()) {
                cardIndex++;
                isSideA = true;
                displayCard();
            }
        }

        public void previousCard() {
            if (!checkFirst()) {
                cardIndex--;
                isSideA = true;
                displayCard();
            }
        }

        public boolean checkFirst() {
            return cardIndex <= 0;
        }

        public boolean checkLast() {
            return ((cardIndex + 1) >= currentCardStack.getCards().size());
        }

        public void flipCard() {
            isSideA = !isSideA;
            displayCard();
        }


    }

    private class NextCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (list.getSelectedIndex() >= 0) {
                currentCardPanel.nextCard();
            }
        }
    }

    private class PreviousCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (list.getSelectedIndex() >= 0) {
                currentCardPanel.previousCard();
            }
        }
    }

    private class FlagCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (list.getSelectedIndex() >= 0) {
                currentCardPanel.currentCard.flagUpdate();
                if (currentCardPanel.currentCard.isFlagged()) {
                    JOptionPane.showMessageDialog(flagCardButton, "Card flagged!");
                } else {
                    JOptionPane.showMessageDialog(flagCardButton, "Card unflagged!");
                }
            }
        }
    }

    private class CardCreatorListener implements ListSelectionListener {
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

    // randomizes Cards in selected card stack.
    private class RandomizeActionListener implements ActionListener {
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

    private class ViewFlaggedCardsListener implements ActionListener {
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

    private class FlipCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (list.getSelectedIndex() >= 0 && !(currentStack.getCards().isEmpty())) {
                currentCardPanel.flipCard();
            }
        }
    }

    private class SaveListener implements ActionListener {
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

    private class LoadListener implements ActionListener {
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

    // TODO: confirm this works: if you can update from image to text dynamically. got it to work in new card listener.
    // TODO: when you add a card when the current stack is set to flaggedcards, it does not update the true list.
    // TODO: style, fonts, centering

}
