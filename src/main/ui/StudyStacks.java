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

public class StudyStacks extends JFrame {
    JMenuBar menuBar;
    JMenu file;
    JMenuItem save;
    JMenuItem load;
    StackList stackList;
    JSplitPane verticalSplitPane;
    JButton newStackButton;
    JButton deleteButton;
    JButton newCardButton;
    JList list;
    DefaultListModel listModel;
    private ArrayList<CardStack> allStacks;
    private CardStack currentStack = null;
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
        JPanel buttonPanel = new JPanel();
        verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        verticalSplitPane.setTopComponent(buttonPanel);
        CurrentCard currentCard = new CurrentCard(currentStack);
        verticalSplitPane.setBottomComponent(currentCard);

        JSplitPane splitPane = new JSplitPane();
        stackList = new StackList();
        splitPane.setLeftComponent(stackList);
        splitPane.setRightComponent(verticalSplitPane);

        buttonPanel.add(newStackButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(newCardButton);
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
                } else {
                    deleteButton.setEnabled(true);
                    newCardButton.setEnabled(true);
                }
            }
        }
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
            allStacks.get(index).getCards().add(newCard);
        }
    }

    private class CurrentCard extends JTextArea {
        public CurrentCard(CardStack cardStack) {
            super();
            this.setEditable(false);
            this.setBackground(Color.white);
            if (cardStack != null) {
                if (cardStack.getCards().isEmpty()) {
                    // display graphic image
                } else {
                    setText(cardStack.getCards().get(0).getSideA());
                }
            }
        }
    }

    public void nextCard() {
        // displays next card in stack
    }

    public void previousCard() {
        // displays previous card in stack
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
            verticalSplitPane.setBottomComponent(new CurrentCard(currentStack));
        }
    }
}
