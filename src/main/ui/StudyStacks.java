package ui;

import model.Card;
import model.CardStack;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static java.awt.Image.*;

public class StudyStacks extends JFrame {
    JMenuBar menuBar;
    JMenu file;
    JMenuItem save;
    JMenuItem load;
    StackList stackList;
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
    CurrentCardPanel currentCardPanel;
    // fields here for components

    public StudyStacks() {
        super("StudyStacks");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));

        allStacks = new ArrayList<>();

        // create a menu component with save and load capabilities
        menuBar = new JMenuBar();
        file = new JMenu("File");
        save = new JMenuItem("Save");
        load = new JMenuItem("Load");
        menuBar.add(file);
        file.add(save);
        file.add(load);
        // add menu bar to the content Pane with JMenuBar.
        setJMenuBar(menuBar);

        // add next functions to frame

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
        flipCardButton.addActionListener(new FlagCardListener());
        initFlagIcon();
        flagCardButton = new JButton(flagIcon);
        flagCardButton.addActionListener(new FlagCardListener());
        JPanel buttonPanel = new JPanel();
        verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        verticalSplitPane.setTopComponent(buttonPanel);
        currentCardPanel = new CurrentCardPanel(currentStack);
        verticalSplitPane.setBottomComponent(currentCardPanel);

        JSplitPane splitPane = new JSplitPane();
        stackList = new StackList();
        splitPane.setLeftComponent(stackList);
        splitPane.setRightComponent(verticalSplitPane);

        buttonPanel.add(newStackButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewFlaggedCardsButton);
        buttonPanel.add(randomizeButton);
        buttonPanel.add(newCardButton);
        buttonPanel.add(flagCardButton);
        buttonPanel.add(nextCardButton);
        buttonPanel.add(previousCardButton);
        buttonPanel.add(flipCardButton);
        add(splitPane);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        StudyStacks studyStacks = new StudyStacks();
    }


    public class StackList extends JPanel implements ListSelectionListener {

        public StackList() {
            listModel = new DefaultListModel();
            CardStack cpsc210 = new CardStack("CPSC 210");
            cpsc210.addCard(new Card("method","function"));
            allStacks.add(cpsc210);
            allStacks.add(new CardStack("Notes on misc."));
            for (CardStack c : allStacks) {
                listModel.addElement(c.getLabel());
            }
            list = new JList(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.addListSelectionListener(this);
            list.addListSelectionListener(new CardCreatorListener());
            list.setVisibleRowCount(10);
            JScrollPane listScrollPane = new JScrollPane(list);
            add(listScrollPane);
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == false) {
                int index = list.getSelectedIndex();
                if (!(index >= 0)) {
                    deleteButton.setEnabled(false);
                    newCardButton.setEnabled(false);
                    randomizeButton.setEnabled(false);
                    nextCardButton.setEnabled(false);
                    previousCardButton.setEnabled(false);
                    flagCardButton.setEnabled(false);
                    viewFlaggedCardsButton.setEnabled(false);
                    flipCardButton.setEnabled(false);
                } else {
                    deleteButton.setEnabled(true);
                    newCardButton.setEnabled(true);
                    randomizeButton.setEnabled(true);
                    nextCardButton.setEnabled(true);
                    previousCardButton.setEnabled(true);
                    flagCardButton.setEnabled(true);
                    viewFlaggedCardsButton.setEnabled(true);
                    flipCardButton.setEnabled(true);
                }
            }
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
            if (listModel.isEmpty()) {
                deleteButton.setEnabled(false);
            } else {
                int index = list.getSelectedIndex();
                allStacks.remove(index);
                listModel.remove(index);
                if (index == listModel.getSize()) {
                    index--;
                }
                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    private class NewStackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String newLabel = JOptionPane.showInputDialog(list, "Name of new stack:",
                    "Create New Stack", JOptionPane.PLAIN_MESSAGE);
            allStacks.add(new CardStack(newLabel));
            listModel.addElement(newLabel);
        }
    }

    private class NewCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String newSideA = JOptionPane.showInputDialog(list, "Side A:",
                    "Create New Card", JOptionPane.PLAIN_MESSAGE);
            String newSideB = JOptionPane.showInputDialog(list, "Side B:",
                    "Create New Card", JOptionPane.PLAIN_MESSAGE);
            Card newCard = new Card(newSideA,newSideB);
            int index = list.getSelectedIndex();
            currentStack.addCard(newCard);
            // fix bug regarding current stack being null at beginning.
            allStacks.get(index).getCards().add(newCard);
        }
    }

    // randomizes Cards in selected card stack.
    private class RandomizeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class CurrentCardPanel extends JPanel {
        CardStack currentCardStack;
        Card currentCard;
        int index = 0;
        JEditorPane cardDisplay;

        public CurrentCardPanel(CardStack cardStack) {
            cardDisplay = new JTextPane();
            cardDisplay.setEditable(false);
            cardDisplay.setBackground(Color.white);
            cardDisplay.setContentType("text/plain");
            this.currentCardStack = cardStack;
            if (cardStack != null) {
                if (cardStack.getCards().isEmpty()) {
                    displayGraphic();
                } else {
                    currentCard = currentCardStack.getCards().get(index);
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
            JLabel splashImage = new JLabel(splashIcon);
            add(splashImage);
        }

        public void displayCard() {
            cardDisplay.setText(currentCard.getSideA());
            add(cardDisplay);
        }

        public void nextCard() {
            index++;
            displayCard();
            // add errors for last card.
        }

        public void previousCard() {
            index--;
            displayCard();
            // add errors for no prev card.
        }

        public void flipCard() {
            cardDisplay.setText(currentCard.getSideB());
            add(cardDisplay);
        }


    }

    private class NextCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentCardPanel.nextCard();
        }
    }

    private class PreviousCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentCardPanel.previousCard();
        }
    }

    private class FlagCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class CardCreatorListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            int index = list.getSelectedIndex();
            try {
                currentStack = allStacks.get(index);
            } catch (Exception ee) {
                currentStack = null;
            }
            verticalSplitPane.setBottomComponent(new CurrentCardPanel(currentStack));
        }
    }

    private class ViewFlaggedCardsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class FlipCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentCardPanel.flipCard();
        }
    }

}
