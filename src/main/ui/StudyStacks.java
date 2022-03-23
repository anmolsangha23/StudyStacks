package ui;

import model.CardStack;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StudyStacks extends JFrame implements ActionListener {
    JMenuBar menuBar;
    JMenu file;
    JMenuItem save;
    JMenuItem load;
    StackList stackList;
    JButton newStackButton;
    JButton deleteButton;
    JList list;
    DefaultListModel listModel;
    private ArrayList<CardStack> allStacks;
    // fields here for components

    public StudyStacks() {
        super("StudyStacks");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));

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
        JPanel buttonPanel = new JPanel();
        JSplitPane horizSplitPane = new JSplitPane();
        horizSplitPane.setTopComponent(buttonPanel);
        // CardView cardview = new CardView();
        horizSplitPane.setBottomComponent(new JLabel("card here"));

        JSplitPane splitPane = new JSplitPane();
        stackList = new StackList();
        splitPane.setLeftComponent(stackList);
        splitPane.setRightComponent(horizSplitPane);

        buttonPanel.add(newStackButton);
        buttonPanel.add(deleteButton);
        add(splitPane);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new StudyStacks();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public class StackList extends JPanel implements ListSelectionListener, ActionListener {

        public StackList() {
            listModel = new DefaultListModel();
            allStacks = new ArrayList<>();
            allStacks.add(new CardStack("CPSC 210"));
            allStacks.add(new CardStack("Notes on misc."));
            for (CardStack c : allStacks) {
                listModel.addElement(c.getLabel());
            }
            list = new JList(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.addListSelectionListener(this);
            list.setVisibleRowCount(10);
            JScrollPane listScrollPane = new JScrollPane(list);
            add(listScrollPane);
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == false) {

                if (!(list.getSelectedIndex() >= 0)) {
                    deleteButton.setEnabled(false);
                } else {
                    deleteButton.setEnabled(true);
                }
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {

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

    private class CardView extends JLabel {
        public CardView(String side) {
            super(side);
        }
    }
}
