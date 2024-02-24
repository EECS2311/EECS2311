package domain.logic;

import gui.HomeView;
import gui.ItemsListView;
import java.util.function.Consumer;

import database.DB;
import domain.logic.Container;
import domain.logic.FoodGroup;
import domain.logic.FoodFreshness;
import domain.logic.Item;
import java.util.EnumSet;
import java.util.Set;

/**
 * Utility class for operations related to items in the inventory such as adding,
 * deleting, and updating item details. This class holds logic that GUI classes can use.
 */
public class ItemUtility {
    /**
     * Verifies and deletes an item from the specified container and updates the
     * Itemslist panel.
     *
     * @param itemName The name of the item to verify and delete.
     * @param con      The container from which to delete the item.
     * @param list     The Itemslist panel to update after deletion.
     * @return Boolean indicating the success or failure of the item deletion.
     */
    public static Boolean verifyDeleteItem(String itemName, Container con, ItemsListView list) {

        // Checking to see if item is in the database
        if (HomeView.data.getItem(con, itemName) != null) {
            // Remove the item if its present
            HomeView.data.removeItem(con, itemName, null);

            // Remove the item from the datalist
            list.removeItem(itemName);
            return true;
        } else
            return false;

    }

    /**
     * Validates the input data for a new item and, if valid, adds it to the ItemsListView panel.
     * In case of validation errors, it utilizes a Consumer to handle the error message.
     *
     * @param name           The name of the new item.
     * @param quantityStr    The quantity of the new item as a string.
     * @param expiryDate     The expiry date of the new item.
     * @param itemsListPanel The ItemsListView panel to which the item will be added.
     * @param errorHandler   A Consumer that handles error messages.
     * @param successCallback A Runnable that is executed upon successful addition.
     */
    public static void verifyAddItem(String name, String quantityStr, String expiryDate, ItemsListView itemsListPanel, Consumer<String> errorHandler, Runnable successCallback) {
        try {
            name = name.trim();
            if (name.isEmpty()) {
                errorHandler.accept("Item name cannot be empty.");
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException ex) {
                errorHandler.accept("Please enter a valid number for quantity.");
                return;
            }

            if (quantity <= 0) {
                errorHandler.accept("Quantity must be greater than 0.");
                return;
            }

            Item item = Item.getInstance(name, quantity, expiryDate);
            itemsListPanel.addItem(item);
            successCallback.run();
        } catch (Exception ex) {
            errorHandler.accept("Error adding item: " + ex.getMessage());
        }
    }

    /**
     * Updates the specified item's properties based on the provided new value and column index.
     * The update is only performed if the item exists in the container.
     *
     * @param data      The DB instance containing item data.
     * @param container The container where the item resides.
     * @param row       The row index in the table where the item is displayed.
     * @param itemName  The name of the item to be updated.
     * @param newValue  The new value to be set for the item's property.
     * @param column    The column index corresponding to the property to be updated.
     * @return true if the item was successfully updated, false otherwise.
     */
    public static boolean updateItem(DB data, Container container, int row, String itemName, Object newValue, int column) {
        Item item = data.getItem(container, itemName);

        if (item == null) {
            return false; // Item not found
        }

        switch (column) {
            case 3: // Food Group column
                if (newValue instanceof FoodGroup) {
                    FoodGroup selectedGroup = (FoodGroup) newValue;
                    Set<FoodGroup> foodGroupSet = EnumSet.of(selectedGroup);
                    item.setFoodGroupTagsEnum(foodGroupSet);
                }
                break;
            case 4: // Food Freshness column
                if (newValue instanceof FoodFreshness) {
                    FoodFreshness selectedFreshness = (FoodFreshness) newValue;
                    item.setFoodFreshnessTag(selectedFreshness);
                }
                break;
            default:
                return false; // Invalid column for update
        }
        return true; // Successfully updated
    }

}
