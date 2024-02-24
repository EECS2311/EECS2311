package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import domain.logic.Container;
import domain.logic.Item;

/**
 * The {@code DB} class represents a simple database for storing and managing
 * containers and their associated items. This class provides methods to add and
 * retrieve containers and items, remove items, and print all items within
 * containers.
 */
public class DB {

	Connection conn;

	/**
	 * Initializes a new database connection.
	 * 
	 * @return Returns the connection object to be used by other methods.
	 * 
	 */
	public Connection init() {
		try {
			conn = DriverManager.getConnection(info.url, info.dbUser, info.dbPass);
			return conn;
		} catch (SQLException e) {
			System.out.println("Connection Failure");
			e.printStackTrace();

		}
		return null;
	}

	public void initDB(Connection conn) {

		try {

			conn.close();
		} catch (SQLException e) {

			System.out.println("Issue inserting item");
			e.printStackTrace();

		}

	}

	private HashMap<String, Container> containers = new HashMap<String, Container>();

	private HashMap<Container, HashMap<String, Item>> items = new HashMap<Container, HashMap<String, Item>>();

	/**
	 * Retrieves a {@link Container} by its name.
	 *
	 * @param containerName The name of the container to retrieve.
	 * @return The {@link Container} object if found, {@code null} otherwise.
	 */
	public Container getContainer(String containerName) {
		return containers.get(containerName);
	}

	/**
	 * Adds a new container to the database.
	 *
	 * @param containerName The name of the container to add.
	 * @param c             The {@link Container} object to be added.
	 */
	public void addContainer(String containerName, Container c) {
		containers.put(containerName, c);

	}

	/**
	 * Adds an item to a specific container.
	 *
	 * @param c    The container to which the item will be added.
	 * @param name The name of the item.
	 * @param ite  The {@link Item} object to be added.
	 */
	public void addItem(Container c, String name, Item ite) {

		// Check if container has associated items.
		HashMap<String, Item> n = items.get(c);
		// If there are no items associated with the container
		if (n != null) {
			n.put(name, ite);

			// If there is no associated hashmap for a given container. It must be a new
			// container.
		} else {
			HashMap<String, Item> m = new HashMap<String, Item>();
			m.put(name, ite);
			items.put(c, m);

		}

	}

	/**
	 * Removes an item from a specified container.
	 *
	 * @param c    The container from which the item will be removed.
	 * @param name The name of the item to be removed.
	 */
	public void removeItem(Container c, String name, Item ite) {

		// Any method that calls removeItem() will ensure that the item exists.
		items.get(c).remove(name);

	}

	/**
	 * Retrieves an {@link Item} by its container and name.
	 *
	 * @param container The container in which the item is stored.
	 * @param itemName  The name of the item to retrieve.
	 * @return The {@link Item} object if found, {@code null} otherwise.
	 */
	public Item getItem(Container container, String itemName) {
		if (items.containsKey(container) && items.get(container).containsKey(itemName)) {
			return items.get(container).get(itemName);
		}
		// Return null if the Container or Item is not found.
		return null;
	}

	/**
	 * Prints all items within each container to the standard output.
	 */
	public void printItems() {
		for (Map.Entry<Container, HashMap<String, Item>> containerEntry : items.entrySet()) {
			Container container = containerEntry.getKey();
			HashMap<String, Item> itemsInContainer = containerEntry.getValue();

			System.out.println("Container: " + container);

			for (Map.Entry<String, Item> itemEntry : itemsInContainer.entrySet()) {
				String itemName = itemEntry.getKey();
				Item item = itemEntry.getValue();

				System.out.println("\tItem Name: " + itemName + " -> Item: " + item);
			}
		}
	}

}
