package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import database.DB;
import domain.logic.Container;
import domain.logic.Item;

class AddAndGetItemTest {

    private DB data;
    private Container container;
    private Item item;

    @BeforeEach
    void setUp() throws Exception {
        data = new DB();
        container = new Container("Vegetables");
        item = Item.getInstance("Carrot", 20);
        data.addContainer("Vegetables", container);
    }

    @Test
    void testAddItem() {
        // Add item to the container in DB
        data.addItem(container, "Carrot", item);

        // Validate that the item can be retrieved and its properties are as expected
        Item retrievedItem = data.getItem(container, "Carrot");
        assertNotNull(retrievedItem, "Item should exist in the database after being added.");
        assertEquals(item.getName(), retrievedItem.getName(), "Retrieved item name should match the added item's name.");
        assertEquals(item.getQuantity(), retrievedItem.getQuantity(), "Retrieved item quantity should match the added item's quantity.");
    }

    @Test
    void testGetItem() {
        // Attempt to retrieve an item that has not been added yet
        Item retrievedItem = data.getItem(container, "Tomato");
        assertNull(retrievedItem, "Item should not exist in the database before being added.");

        // Now add the item and retrieve it
        data.addItem(container, "Tomato", Item.getInstance("Tomato", 15));
        retrievedItem = data.getItem(container, "Tomato");
        assertNotNull(retrievedItem, "Item should exist in the database after being added.");
        assertEquals("Tomato", retrievedItem.getName(), "Retrieved item name should match the added item's name.");
        assertEquals(15, retrievedItem.getQuantity(), "Retrieved item quantity should match the added item's quantity.");
    }

    @Test
    void testAddAndGetItem() {
        DB data = new DB();
        Container c = new Container("Fruits");
        Item item = Item.getInstance("Apple", 10);
        data.addContainer("Fruits", c); // Ensure the container is added to the database
        data.addItem(c, "Apple", item); // Add item to the container in DB

        // Validate that the item can be retrieved and its properties are as expected
        Item retrievedItem = data.getItem(c, "Apple");
        assertNotNull(retrievedItem, "Item should exist in the database after being added.");
        assertEquals(item.getName(), retrievedItem.getName(), "Retrieved item name should match the added item's name.");
        assertEquals(item.getQuantity(), retrievedItem.getQuantity(), "Retrieved item quantity should match the added item's quantity.");
    }
}
