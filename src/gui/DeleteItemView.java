package gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import domain.logic.Container;
import domain.logic.ItemUtility;

/**
 * Provides a graphical interface for deleting an item from a specified
 * container. This class displays a dialog asking the user to enter the name of
 * the item they wish to delete and performs the deletion if the item exists
 * within the container.
 */
public class DeleteItemView extends JPanel {

	private JFrame deleteFrame;
	private JPanel deletePanel;
	private JButton confirm;
	private JLabel t;
	private JTextField searchField;

	/**
	 * Constructs a DeleteItemView and initiates the item deletion process.
	 * 
	 * @param containerPanel The panel from which the delete action was initiated.
	 * @param c              The container from which an item is to be deleted.
	 * @param itemsListPanel The Itemslist panel showing the list of items.
	 */
	public DeleteItemView(JPanel containerPanel, Container c, ItemsListView itemsListPanel) {

		String item = null;
		Boolean success;

		item = JOptionPane.showInputDialog(containerPanel, "Type the name of the item to delete",
				"*Delete Item from Pantry", 3);

		success = ItemUtility.verifyDeleteItem(item, c, itemsListPanel);

		if (success) {
			JOptionPane.showMessageDialog(containerPanel, "Item Successfully Deleted!", item, 3);
			item = null;

		} else {
			if (item != null) {
				JOptionPane.showMessageDialog(containerPanel, "This item does not exist in your pantry!", item, 0);
			}

		}

	}

}
