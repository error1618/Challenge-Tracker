import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ExpenseTrackerGUI extends JFrame implements ActionListener {
    private JTextField descriptionField, dateField, amountField;
    private JTextArea displayArea;
    private JComboBox<String> categoryDropdown;
    private JButton addButton, removeButton, showTotalButton, manageCategoriesButton;

    public ExpenseTrackerGUI() {
        setTitle("Expense Tracker");
        setSize(500, 400);
        setLocationRelativeTo(null); // center the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        descriptionField = new JTextField();
        dateField = new JTextField();
        amountField = new JTextField();
        displayArea = new JTextArea();
        displayArea.setEditable(false); // prevent editing of displayed text
        categoryDropdown = new JComboBox<String>(new String[]{"Category 1", "Category 2", "Category 3"});
        addButton = new JButton("Add Expense");
        removeButton = new JButton("Remove Expense");
        showTotalButton = new JButton("Show Total");
        manageCategoriesButton = new JButton("Manage Categories");

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Date:"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryDropdown);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(showTotalButton);
        buttonPanel.add(manageCategoriesButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(this);
        removeButton.addActionListener(this);
        showTotalButton.addActionListener(this);
        manageCategoriesButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String description = descriptionField.getText();
            String date = dateField.getText();
            String amountStr = amountField.getText();
            double amount = Double.parseDouble(amountStr);
            String category = (String) categoryDropdown.getSelectedItem();
        } else if (e.getSource() == removeButton) {
        } else if (e.getSource() == showTotalButton) {
        } else if (e.getSource() == manageCategoriesButton) {
        }
    }

    public static void main(String[] args) {
        ExpenseTrackerGUI gui = new ExpenseTrackerGUI();
        gui.setVisible(true);
    }
}
