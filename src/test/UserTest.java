package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.logic.Container;
import domain.logic.Item;
import domain.logic.User;

class UserTest {
	private User user;
    private Container container;
    private Item item;

    @BeforeEach
    public void setUp() {
        user = User.getInstance("John");
        container = new Container("Box");
        item = Item.getInstance("Apple");
        container.addNewItem(item);
        user.addContainer(container);
    }

    @Test
    public void testGetName() {
        assertEquals("John", user.getName());
    }

    @Test
    public void testSetName() {
        user.setName("Smith");
        assertEquals("Smith", user.getName());
    }

    @Test
    public void testGetInstance() {
        User newUser = User.getInstance("Alice");
        assertEquals("Alice", newUser.getName());
    }
    
    @Test
    public void testGetContainerList() {
    	ArrayList<Container> expected = new ArrayList<>();
    	expected.add(container);
    	assertEquals(expected, user.getListOfContainers());
    }

    @Test
    public void testAddItemToUser() {
        Item newItem = Item.getInstance("Banana");
        user.addItemToUser("Box", newItem);
        assertTrue(container.getItems().contains(newItem));
    }

    @Test
    public void testRemoveItemFromUser() {
        user.removeItemFromUser("Box", item);
        assertFalse(container.getItems().contains(item));
    }

    @Test
    public void testAddItemToUserWithInvalidContainer() {
        Item newItem = Item.getInstance("Pear");
        assertThrows(IllegalArgumentException.class, () -> {
            user.addItemToUser("InvalidContainer", newItem);
        });
    }

    @Test
    public void testRemoveItemFromUserWithInvalidContainer() {
        assertThrows(IllegalArgumentException.class, () -> {
            user.removeItemFromUser("InvalidContainer", item);
        });
    }
    @Test
    public void testFindContainer() {
    	assertEquals(container, user.findContainer("Box"));
    }
    @Test
    public void testFindContainerWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            user.findContainer("");
        });
    }
}

