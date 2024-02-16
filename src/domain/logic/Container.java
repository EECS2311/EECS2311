package domain.logic;

import gui.ContainerView;
import gui.Home;

import java.util.ArrayList;

public class Container {
	private String name;
	private ArrayList<Item> listOfItems;
	private ContainerView containerViewgui;

	public Container(String n, Home home) {
		this.name = n;
		listOfItems = new ArrayList<>();
		containerViewgui = new ContainerView(home); // Pass Home instance to ContainerView
	}

	public String getName() {
		return name;
	}

	/**
	 * Add new items into the container
	 */
	public void addNewItem(Item item) {
		listOfItems.add(item);
	}

	public void getGUI() {
		containerViewgui.setupContainerViewGUI(true);
	}

	public void setName(String nameOfContainer) {
		this.name = nameOfContainer;
	}
	
	/**
	 * Returns a deep copy of the container's list of items
	 * @return a deep copy of the container's list of items
	 */
	public ArrayList<Item> getListofItems() {
		ArrayList<Item> itemsCopy = new ArrayList<Item>();
		for (Item i : listOfItems) {
			Item copy = Item.getInstance(i);
			itemsCopy.add(copy);
		}
		return itemsCopy;
	}
}