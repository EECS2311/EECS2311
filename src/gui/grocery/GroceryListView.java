package gui.grocery;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import database.DB;
import gui.home.HomeView;

public class GroceryListView extends JPanel implements ActionListener {
	// Data model and components
    private DefaultTableModel tableModel;
    private JTable table;
    private DB data;
    private static GroceryListView groceryListView;

    private JLabel titleLabel = new JLabel("Grocery List");
    private JLabel addItemLabel = new JLabel("Item name:");

    private JPanel viewOfAllPanel = new JPanel();
    private JPanel tablePanel = new JPanel();
    private JPanel buttonsPanel = new JPanel();
    private JPanel topPanel = new JPanel();

    // Create a text field for adding items
    JTextField addItemTextField = new JTextField();

    // Maintain a set of crossed off items
    private Set<String> crossedOffItems;

    // Buttons for interacting with the grocery list
    JButton addButton = new JButton("Add Item");
    JButton backButton = new JButton("Back");
    JButton exportButton = new JButton("Export to .txt");
    
    /**
     * BoxLayout of buttonsPanel
     */
    private JPanel boxLayoutButtonsPanel = new JPanel();

    /**
	 * Launches the application and initializes the main GUI components.
	 */
    public GroceryListView() {
    	groceryListView = this;
        this.data = HomeView.data;
        this.crossedOffItems = new HashSet<>();

        // Initialize the view panels
        viewOfAllPanel.setLayout(null);
        viewOfAllPanel.setBackground(new Color(253, 241, 203));

        tablePanel.setLayout(null); // Set layout to null for absolute positioning
        tablePanel.setBackground(new Color(253, 241, 203));

        buttonsPanel.setLayout(null); // Set layout to null for absolute positioning
        buttonsPanel.setBackground(new Color(253, 241, 203));

        topPanel.setLayout(null); // Set layout to null for absolute positioning
        topPanel.setBackground(new Color(253, 241, 203));

        //Add title Label and back button to the top panel
        topPanel.add(titleLabel);
        topPanel.add(backButton);

        // Add panels and label to the main panel
        viewOfAllPanel.add(tablePanel);
        viewOfAllPanel.add(buttonsPanel);
        viewOfAllPanel.add(topPanel);

        // Set positions and sizes of components
        tablePanel.setBounds(10, 80, 800, 300);
        buttonsPanel.setBounds(10, 400, 800, 200);
        topPanel.setBounds(10, 0, 800, 100);
        titleLabel.setBounds(300, 0, 600, 100);
        titleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 30));
        backButton.setBounds(20, 20, 80, 30);

        // Initialize the table model
        tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"Item Name"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
            	return false;
            }
        };
        // Create a JTable with the specified table model
        table = new JTable(tableModel);
        
        // Set the cell renderer for the table
        table.setDefaultRenderer(Object.class, new StrikeThroughRenderer(table.getFont(), crossedOffItems));

        // Create a scroll pane to hold the table, allowing scrolling if needed
        JScrollPane scrollPane = new JScrollPane(table);

        // Set the bounds of the scroll pane (x, y, width, height)
        scrollPane.setBounds(10, 10, 760, 300);

        // Add the scroll pane to the table panel
        tablePanel.add(scrollPane);
        
        boxLayoutButtonsPanel.setLayout(new BoxLayout(boxLayoutButtonsPanel, BoxLayout.Y_AXIS));

        // Set the position of buttons and add text field
        boxLayoutButtonsPanel.add(addItemLabel);
        boxLayoutButtonsPanel.add(addItemTextField);
        boxLayoutButtonsPanel.add(addButton);
        
        JScrollPane scrollBoxLayout = new JScrollPane(boxLayoutButtonsPanel);
        boxLayoutButtonsPanel.setBackground(new Color(253, 241, 203));
        scrollBoxLayout.setBounds(10, 0, 300, 150);
        exportButton.setBounds(500, 0, 140, 30);
        
        //Add buttons and add text field to the buttons panel
        buttonsPanel.add(exportButton);
        buttonsPanel.add(scrollBoxLayout);

    }
    
    /**
     * sets fonts of components and resizes if needed
     */
    public void addFonts() {
    	Font f = new Font("Lucida Grande", Font.PLAIN, HomeView.getSettings().getFontSize());
    	
        table.setFont(f);
        table.setRowHeight(f.getSize() + 5);
        table.setDefaultRenderer(Object.class, new StrikeThroughRenderer(f, crossedOffItems));

        backButton.setFont(f);
        int width = (int) (f.getSize() *5);
        backButton.setBounds(20, 20, width, f.getSize());
        
        exportButton.setFont(f);
        width = (int) (f.getSize() *7.5);
        exportButton.setBounds(400, 0, width, f.getSize() + 10);
        
        addItemLabel.setFont(f);
        addItemTextField.setFont(f);
        addButton.setFont(f);
        

    }

    /**
	 * Sets the visibility of the GroceryListView GUI depending on the boolean passed
	 * @param b the value of whether the visibility is true or not
	 */
    public void setGroceryListViewVisibility(boolean b) {
        if (b) {
            HomeView.getHomeView().setHomeViewVisibility(false);

            // Attach action listeners to buttons
            addButton.addActionListener(this);
            exportButton.addActionListener(this);
            backButton.addActionListener(this);

            // Refresh the table
            refreshTable();

            
            //Add all panels
            HomeView.getFrame().add(viewOfAllPanel);

            // Add right-click event handler for table rows
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        int r = table.rowAtPoint(e.getPoint());
                        if (r >= 0 && r < table.getRowCount()) {
                            table.setRowSelectionInterval(r, r);
                        } else {
                            table.clearSelection();
                        }

                        JPopupMenu popup = createPopupMenu();
                        popup.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });

            addFonts();

            // Set visibility to true
            viewOfAllPanel.setVisible(true);

        } else {
            // Remove action listeners
            addButton.removeActionListener(this);
            exportButton.removeActionListener(this);
            backButton.removeActionListener(this);

            // Hide the view
            viewOfAllPanel.setVisible(false);
        }
    }

    /**
     * Adds an item to the table and database.
     */
    private void addItem() {
        String itemName = addItemTextField.getText();
        if (itemName != null && !itemName.trim().isEmpty()) {
            try {
                data.addToGroceryList(itemName);
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error adding item: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Removes the selected item from the table and database.
     */
    private void removeItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String itemName = (String) tableModel.getValueAt(selectedRow, 0);
            data.removeFromGroceryList(itemName);
            crossedOffItems.remove(itemName);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to remove.");
        }
        refreshTable();
    }

    /**
     * Export the grocery list to a text file.
     */
    private void exportToTxt() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("grocery_list.txt"));
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                writer.write((String) tableModel.getValueAt(i, 0));
                writer.newLine();
            }
            writer.close();
            JOptionPane.showMessageDialog(this, "Grocery list exported successfully to grocery_list.txt", "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error exporting grocery list: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Refreshes the table to update it.
     */
    private void refreshTable() {
        // Clear the existing table data
        tableModel.setRowCount(0);

        // Retrieve the updated data from the database
        Object[][] newData = data.getAllGroceryItems(); // Implement this method in your DB class

        // Add the updated data to the table model
        for (Object[] row : newData) {
            tableModel.addRow(row);
        }

        // Repaint the table to reflect the changes
        table.repaint();
    }

    /**
	 * Provides access to this instance of GroceryListView
	 *
	 * @return the current instance of GroceryListView
	 */
	public static GroceryListView getGroceryListView() {
		return groceryListView;
	}

	/**
	 * Handles action events triggered by various GUI components.
	 * This method is the central hub for processing user interactions within the grocery list view.
	 *
	 * @param e The ActionEvent object containing details about the event.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source ==  backButton) {
			HomeView.getHomeView().setHomeViewVisibility(true);
			setGroceryListViewVisibility(false);
		} else if (source == addButton) {
			addItem();
		} else if (source == exportButton){
			exportToTxt();
		}
	}
	private JPopupMenu createPopupMenu() {
	    JPopupMenu popupMenu = new JPopupMenu();

	    // Create menu items
	    JMenuItem strikethroughItem = new JMenuItem("Cross off/Uncross off");
	    JMenuItem deleteItem = new JMenuItem("Delete");

	    // ActionListener for strikethroughItem
	    strikethroughItem.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            String selectedItem = (String) table.getValueAt(table.getSelectedRow(), 0);
	            if (selectedItem != "" & selectedItem != null) {
	                if (crossedOffItems.contains(selectedItem)) {
	                    crossedOffItems.remove(selectedItem);
	                } else {
	                    crossedOffItems.add(selectedItem);
	                }
	                table.repaint();
	            }
	        }
	    });

	    // ActionListener for deleteItem
	    deleteItem.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            removeItem(); // Call removeItem method
	        }
	    });

	    // Add menu items to the popup menu
	    popupMenu.add(strikethroughItem);
	    popupMenu.add(deleteItem);

	    return popupMenu;
	}

    class StrikeThroughRenderer extends DefaultTableCellRenderer {
    	private Font font;
        private Set<String> crossedOffItems;

        public StrikeThroughRenderer(Font font, Set<String> crossedOffItems) {
            this.font = font;
            this.crossedOffItems = crossedOffItems;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String itemName = (String) value;
            if (crossedOffItems.contains(itemName)) {
                Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
                attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                Font newFont = font.deriveFont(attributes);
                c.setFont(newFont);
            } else {
                c.setFont(font);
            }
            return c;
        }
    }
}