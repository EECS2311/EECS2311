package test.java.integration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import main.java.database.DB;

class DBTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}

	@Test
	void testConnection() {
		DB data = new DB();

		assertDoesNotThrow(() -> data.init());

	}

}
