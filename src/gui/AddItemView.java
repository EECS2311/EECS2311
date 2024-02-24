package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import domain.logic.Item;
import database.DB;
import domain.logic.Container;
import domain.logic.ItemUtility;

/**
 * A JPanel subclass that provides a user interface for adding items with name, quantity, and expiration date.
 * It validates user input, adds created items to a provided list, and updates a display area to show all added items.
 */
public class AddItemView extends JPanel {
    private JTextField itemNameField = new JTextField(10);
    private JTextField itemQuantityField = new JTextField(5);
    private JTextField itemExpiryField = new JTextField(10);
    private JButton addButton = new JButton("Add Item");

    private ItemsListView itemsListPanel;

    /**
     * Constructs a new AddItemPanel with references to a list of items and a display area.
     *
     * @param itemsListPanel The text area where details of added items will be displayed.
     */
    public AddItemView(ItemsListView itemsListPanel) {
        this.itemsListPanel = itemsListPanel; // Initialize the reference

        setLayout(new FlowLayout());
        add(new JLabel("Item Name:"));
        add(itemNameField);
        add(new JLabel("Quantity:"));
        add(itemQuantityField);
        add(new JLabel("Expiration Date (dd-MMMM-yyyy):"));
        add(itemExpiryField);
        add(addButton);

        addButton.addActionListener(e -> addItem());
    }

    /**
     * Attempts to add a new item based on the user input from the text fields.
     * Validates the input and shows error messages if the validation fails.
     * If the input is valid, creates a new Item, adds it to the items list, updates the display area,
     * and clears the input fields for the next entry.
     */
    private void addItem() {
        ItemUtility.verifyAddItem(
                itemNameField.getText(),
                itemQuantityField.getText(),
                itemExpiryField.getText(),
                itemsListPanel,
                (errorMsg) -> JOptionPane.showMessageDialog(this, errorMsg, "Input Error", JOptionPane.ERROR_MESSAGE),
                () -> {
                    itemNameField.setText("");
                    itemQuantityField.setText("");
                    itemExpiryField.setText("");
                }
        );
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, 100); // You can adjust the height as needed.
    }


}
