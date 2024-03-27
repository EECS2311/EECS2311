package domain.logic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.table.DefaultTableModel;

import database.DB;
import gui.HomeView;

/**
 * Utility class for operations related to items in the inventory such as
 * adding, deleting, and updating item details. This class holds logic that GUI
 * classes can use.
 */
public class ItemUtility {
	/**
	 * Verifies the deletion of an item from the specified container
	 *
	 * @param itemName  The name of the item to verify and delete.
	 * @param container The container from which to delete the item.
	 * @return Boolean indicating the success or failure of the item deletion.
	 */
	public static Boolean verifyDeleteItem(String itemName, Container container) {

		// Checking to see if item is in the database
		if (HomeView.data.getItem(container, itemName) != null) {
			// Remove the item if its present
			HomeView.data.removeItem(container, itemName, null);

			return true;
		} else
			return false;

	}

	/**
	 * Validates the input data for a new item and, if valid, returns true. In case
	 * of validation errors, it utilizes a Consumer to handle the error message and
	 * returns false.
	 *
	 * @param name         The name of the new item.
	 * @param quantityStr  The quantity of the new item as a string.
	 * @param expiryDate   The expiry date of the new item.
	 * @param errorHandler A Consumer that handles error messages.
	 */
	public static boolean verifyAddItem(String name, String quantityStr, String expiryDate,
			Consumer<String> errorHandler) {
		try {
			name = name.trim();
			if (name.isEmpty()) {
				errorHandler.accept("Item name cannot be empty.");
				return false;
			}

			int quantity;
			try {
				quantity = Integer.parseInt(quantityStr);
			} catch (NumberFormatException ex) {
				errorHandler.accept("Please enter a valid number for quantity.");
				return false;
			}

			if (quantity <= 0) {
				errorHandler.accept("Quantity must be greater than 0.");
				return false;
			}

			Item item = Item.getInstance(name, quantity, expiryDate);

			// Validation successful, return true
			return true;
		} catch (Exception ex) {
			errorHandler.accept("Error validating item: " + ex.getMessage());
			return false;
		}
	}

	/**
	 * Updates the specified item's food group tag based on the provided new value
	 * and column index. The update is only performed if the item exists in the
	 * container.
	 *
	 * @param container The container where the item resides.
	 * @param itemName  The name of the item to be updated.
	 * @param newValue  The new value to be set for the item's property.
	 * @param column    The column index corresponding to the property to be
	 *                  updated.
	 * @return true if the item was successfully updated, false otherwise.
	 */
	public static void updateItemFoodGroupTag(Container container, String itemName, Object newValue, int column) {
		if (column == 3 && newValue instanceof FoodGroup) {
			HomeView.data.updateItemFoodGroup(container, itemName, (FoodGroup) newValue);
		}
	}

	/**
	 * Retrieves and initializes the rows in ItemsListViews from the database for a
	 * specified container.
	 * 
	 * @param c          Container object to initialize the items for
	 * @param tableModel the table object to initialize the rows for
	 */
	public static void initItems(Container c, DefaultTableModel tableModel) {
		List<Item> items = HomeView.data.retrieveItems(c);
		tableModel.setRowCount(0);
		for (Item item : items) {
			tableModel.addRow(new Object[] { item.getName(), item.getQuantity(), dateFormat(item.getExpiryDate()),
					item.getFoodGroupTag(), item.getFoodFreshnessTag() });
		}
	}

	/**
	 * Formats a Date object into a string representation with the format
	 * "yyyy-MM-dd". This format is often used for SQL date columns.
	 *
	 * @param expiryDate The date to be formatted.
	 * @return A string representation of the date in "yyyy-MM-dd" format.
	 */
	private static String dateFormat(Date expiryDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String format = formatter.format(expiryDate);
		return format;
	}

	/**
	 * Assigns FoodFreshness tags to items based on their expiry dates.
	 * 
	 * @param container The container whose items' freshness will be updated.
	 */
	public static void assignFoodFreshness(Container container) {
		HomeView.data.batchUpdateItemFreshness(container);
	}

	/**
	 * Retrieves storage tips for a specific food item from the database. This
	 * method queries the database through the {@code HomeView.data} interface to
	 * find storage tips associated with the given food name. If a tip is found, it
	 * is returned as a string.
	 *
	 * @param foodName The name of the food item for which storage tips are being
	 *                 retrieved.
	 * @return A string containing storage tips for the specified food item. Returns
	 *         {@code null} if no tips are found or if there's an error in
	 *         retrieving the data.
	 */
	public static String retrieveStorageTip(String foodName) {
		return HomeView.data.getStorageTip(foodName);

	}

	/**
	 * Verifies and updates the quantity of an item in the list.
	 * 
	 * @param val             The user inputed value.
	 * @param data            The database object
	 * @param c               The container that belongs to the item
	 * @param item            The name of the item which needs the quantity edited
	 * @param errorHandler    A Consumer thsat handles error messages.
	 * @param successCallback A Runnable that is executed upon successful addition.
	 */
	public static void verifyEditQuantity(String val, DB data, Container c, String item, Consumer<String> errorHandler,
			Runnable successCallback) {
		try {
			if (val == null) {
				return;
			}
			val = val.trim();
			if (val.isEmpty()) {
				errorHandler.accept("Quantity cannot be empty!");
				return;
			}
			int o = Integer.parseInt(val);

			if (o < 0) {
				errorHandler.accept("You can't have a negative quantity!");
				return;
			} else if (o == 0) {
				verifyDeleteItem(item, c);
				successCallback.run();

			} else {
				data.updateQuantity(item, o, c);
				successCallback.run();
			}

		} catch (NumberFormatException e) {
			errorHandler.accept("Not a valid number!");
			return;
		}

	}

	public static void updateFreshness() {
		DB db = HomeView.data;

		List<String> s = db.retrieveContainers();
		Container c;
		for (String containerName : s) {

			c = new Container(containerName);
			db.batchUpdateItemFreshness(c);
		}
	}
}
