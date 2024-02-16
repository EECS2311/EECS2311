package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import domain.logic.Item;

/**
 * A JPanel subclass that provides a user interface for adding items with name, quantity, and expiration date.
 * It validates user input, adds created items to a provided list, and updates a display area to show all added items.
 */
public class AddItemPanel extends JPanel {
    private JTextField itemNameField = new JTextField(10);
    private JTextField itemQuantityField = new JTextField(5);
    private JTextField itemExpiryField = new JTextField(10);
    private JButton addButton = new JButton("Add Item");

    private List<Item> items;
    private JTextArea displayArea;

    /**
     * Constructs a new ui.AddItemPanel with references to a list of items and a display area.
     *
     * @param items The list where added items will be stored.
     * @param displayArea The text area where details of added items will be displayed.
     */
    public AddItemPanel(List<Item> items, JTextArea displayArea) {
        this.items = items;
        this.displayArea = displayArea;

        setLayout(new FlowLayout());
        add(new JLabel("Item Name:"));
        add(itemNameField);
        add(new JLabel("Quantity:"));
        add(itemQuantityField);
        add(new JLabel("Expiration Date (dd-MMM-yyyy):"));
        add(itemExpiryField);
        add(addButton);

        updateDisplayArea();

        addButton.addActionListener(e -> addItem());
    }

    /**
     * Attempts to add a new item based on the user input from the text fields.
     * Validates the input and shows error messages if the validation fails.
     * If the input is valid, creates a new Item, adds it to the items list, updates the display area,
     * and clears the input fields for the next entry.
     */
    private void addItem() {
        try {
            String name = itemNameField.getText().trim(); // Trim to remove leading and trailing spaces
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Item name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return; // Stop the method execution if the name is empty
            }

            int quantity;
            try {
                quantity = Integer.parseInt(itemQuantityField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return; // Stop the method execution if the quantity is not a valid number
            }

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return; // Stop the method execution if the quantity is less than or equal to 0
            }

            String expiryDate = itemExpiryField.getText();

            // Create and add the item
            Item item = Item.getInstance(name, quantity, expiryDate);
            items.add(item);

            // Update display area
            updateDisplayArea();

            // Clear the fields for new inputs
            itemNameField.setText("");
            itemQuantityField.setText("");
            itemExpiryField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding item: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates the display area with the current list of items.
     * Iterates over all items in the list, appending their string representation to the display area.
     */
    private void updateDisplayArea() {
        StringBuilder sb = new StringBuilder();
        for (Item item : items) {
            sb.append(item).append("\n");
        }
        displayArea.setText(sb.toString());
    }

}
