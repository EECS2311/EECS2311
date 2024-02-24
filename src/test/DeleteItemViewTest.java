package test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import database.DB;
import domain.logic.Container;
import domain.logic.Item;

class DeleteItemViewTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@Test
	void testRemove() {
		DB data = new DB();
		Container c = new Container("Cheese");
		Item item = Item.getInstance("Feta", 25);
		data.addContainer("Cheese", c);
		data.addItem(c, "Feta", item);

		assertNotNull(data.getItem(c, "Feta"));
		data.removeItem(c, "Feta", item);

		assertTrue(data.getItem(c, "Feta") == null);

	}

}
