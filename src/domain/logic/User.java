package domain.logic;
import java.util.*;
/*
 * A class that lets the client create and modify users.
 */
public class User {
	private String name;
	private ArrayList<Container> listOfContainers;
	private ArrayList<Item> listOfItems;
	// Constructors are private to enforce the use of factory methods for instance creation.
	/*
	 * Constructor that adds a default new user.
	 */
	private User() {
		this.setName("User");
		this.listOfContainers = new ArrayList<>();
	}
	/*
	 * Constructor that adds a new user where the name is given.
	 * @param name The name of the user.
	 */
	private User(String name) {
		this.setName(name);
		this.listOfContainers = new ArrayList<>();
	}
	/*
	 * Constructor that adds a new user where the name and the list of items owned is given.
	 * @param name The name of the user.
	 * @param listOfItems The list of items owned by the user.
	 */
	private User(String name, ArrayList<Container> listOfContainers) {
		this.setName(name);
		this.listOfContainers = new ArrayList<>();
		for(Container container : listOfContainers) {
			this.listOfContainers.add(container);
			this.listOfItems.addAll(container.getItems());
			
		}
	}
	/*
	 * Constructor that adds a new user by copying an existing user.
	 * @param user The user being copied.
	 */
	private User(User user) {
		this.setName(user.getName());
		this.listOfContainers = new ArrayList<>();
		ArrayList<Container> userContainers = user.getListOfContainers();
		for(Container container : userContainers) {
			this.listOfContainers.add(container);
			this.listOfItems.addAll(container.getItems());
		}
	}
	/*
	 * Factory method to create an instance of User with default properties.
	 */
	public static User getInstance() {
		User user = new User();
		return user;
	}
	/*
	 * Factory method to create an instance of User with assigned name.
	 * @param name The name of the user.
	 * @return New user object.
	 */
	public static User getInstance(String name) {
		User user = new User(name);
		return user;
	}
	/*
	 * Factory method to create an instance of User with assigned name and list of containers.
	 * @param name The name of the user.
	 * @param listOfItems The list of containers owned by the user.
	 * @return New user object.
	 */
	public static User getInstance(String name, ArrayList<Container> listOfContainers) {
		User user = new User(name, listOfContainers);
		return user;
	}
	/*
	 * Gets the name of the user.
	 * @return The name of the user.
	 */
	public String getName() {
		return this.name;
	}
	/*
	 * Modifies the name of the user.
	 * @param name The new name assigned to the user.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/*
	 * Adds a new container to the user.
	 * @param container The container to be added.
	 */
	public void addContainer(Container container) {
		this.listOfContainers.add(container);
	}
	/*
	 * Gives a list of all the containers owned by the user.
	 * @return the list of containers owned by the user. 
	 */
	public ArrayList<Container> getListOfContainers() {
		ArrayList<Container> list = new ArrayList<>();
		for(Container container : this.listOfContainers) {
			Container con = new Container(container);
			list.add(con);
		}
		return list;
	}
	/*
	 * Gives a list of items owned by the user.
	 * @return The list of items owned by the user.
	 */
	public ArrayList<Item> getListOfItems(){
		ArrayList<Item> list = new ArrayList<>();
		for(Item item : this.listOfItems) {
			list.add(item);
		}
		return list;
	}
	/*
	 * Finds the container by name in the user's list of containers.
	 * @param name Name of the container.
	 * @return Returns the container if user owns the container, returns null if user does not.
	 * @throws IllegalArgumentException If the name is empty.
	 */
	public Container findContainer(String name) {
		if(name.isEmpty()) {
			throw new IllegalArgumentException();
		}
		for(Container container: this.listOfContainers) {
			if(container.getName().equals(name)) {
				return container;
			}
		}
		return null;
	}
	/*
	 * Adds an item to a container owned by the user.
	 * @param containerName The name of the container that the item will be stored in.
	 * @param item The item to be added.
	 * @throws IllegalArgumentException If the user does not own the container.
	 */
	public void addItemToUser(String containerName, Item item) {
		Container con = findContainer(containerName);
		if(con != null) {
			con.addNewItem(item);
		}
		else {
			throw new IllegalArgumentException();
		}
	}
	/*
	 * Removes an item from a container owned by the user.
	 * @param containerName The name of the container that the item will be stored in.
	 * @param item The item to be removed.
	 * @throws IllegalArgumentException If the user does not own the container.
	 */
	public void removeItemFromUser(String containerName, Item item) {
		Container con = findContainer(containerName);
		if(con != null) {
			con.removeItem(item);
		}
		else {
			throw new IllegalArgumentException();
		}
	}
}
