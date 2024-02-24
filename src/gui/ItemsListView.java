package gui;

import java.awt.BorderLayout;
import java.util.EnumSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import database.DB;
import domain.logic.*;

/**
 * Represents a panel that displays a list of items within a container.
 * It provides functionalities to add and remove items, and update item properties directly from the table.
 */
public class ItemsListView extends JPanel {
	private DefaultTableModel tableModel;
	private JTable table;
	// private List<Item> items;

	private HomeView home;

	private DB data;
	private Container container;

	/**
	 * Constructs an ItemsListView panel associated with a specific container.
	 * @param home The reference to the Home GUI, allowing for interaction with the main application frame.
	 * @param container The container whose items are to be displayed and managed.
	 */
	public ItemsListView(HomeView home, Container container) {
		this.home = home;
		this.data = home.data;
		this.container = container;
		setLayout(new BorderLayout());

		// Initialize the table model
		tableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				//  "Name" is at index 0, "Quantity" is at index 1, and "Expiry Date" is at index 2
				return !(column == 0 || column == 1 || column == 2); // columns 0, 1, 2 are not editable
			}

			// Enforce a certain type for each column
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				switch (columnIndex) {
					case 0:
						return String.class;
					case 1:
						return Integer.class;
					case 2:
						return String.class;
					case 3:
						return FoodGroup.class;
					case 4:
						return FoodFreshness.class;
					default:
						return Object.class;
				}
			}
		};

		table = new JTable(tableModel);
		// items = new ArrayList<>();

		// Define table columns
		tableModel.addColumn("Name");
		tableModel.addColumn("Quantity");
		tableModel.addColumn("Expiry Date");
		tableModel.addColumn("Food Group");
		tableModel.addColumn("Food Freshness");

		// Add a model listener to capture changes to the table model
		tableModel.addTableModelListener(e -> {
			if (e.getType() == TableModelEvent.UPDATE) {
				int row = e.getFirstRow();
				int column = e.getColumn();
				if (row >= 0 && column >= 0) {
					updateItemFromTable(row, column);
				}
			}
		});

		// Populate JComboBoxes with enum values
		JComboBox<FoodGroup> foodGroupComboBox = new JComboBox<>(FoodGroup.values());
		JComboBox<FoodFreshness> foodFreshnessComboBox = new JComboBox<>(FoodFreshness.values());

		// Set up the food group and food freshness columns with JComboBoxes
		table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(foodGroupComboBox));
		table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(foodFreshnessComboBox));

		add(new JScrollPane(table), BorderLayout.CENTER);
	}


	/**
	 * Adds an item to the table and updates the underlying container.
	 * @param item The item to be added.
	 */
	public void addItem(Item item) {
		tableModel.addRow(
				new Object[] { item.getName(), item.getQuantity(), item.getExpiryDate().toString(), null, null });
		data.addItem(container, item.getName(), item); // Keep track of the added item
		// home.data.printItems();

	}

	/**
	 * Removes an item from the table based on its name.
	 * @param itemName The name of the item to be removed.
	 */
	public void removeItem(String itemName) {
		// Iterate through the table to find the row with the given item name
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			if (itemName.equals(tableModel.getValueAt(i, 0))) {

				// Remove the row from the table model
				tableModel.removeRow(i);
				break;
			}
		}
	}

	/**
	 * Updates an item's properties based on changes made within the table.
	 * @param row The row index of the item in the table.
	 * @param column The column index of the property that was changed.
	 */
	private void updateItemFromTable(int row, int column) {
		String itemName = (String) table.getModel().getValueAt(row, 0);
		Object newValue = table.getModel().getValueAt(row, column);

		boolean updateSuccess = ItemUtility.updateItem(data, container, row, itemName, newValue, column);

		if (updateSuccess) {
			// home.data.printItems();
			JOptionPane.showMessageDialog(this, "Item updated successfully.", "Update", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Error updating item. Please check the values.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
