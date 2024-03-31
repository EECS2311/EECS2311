package database;



import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import domain.logic.container.Container;
import domain.logic.item.FoodFreshness;
import domain.logic.item.FoodGroup;
import domain.logic.item.GenericTag;
import domain.logic.item.Item;
import domain.logic.recipe.Ingredient;
import domain.logic.recipe.Recipe;

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

	/**
	 * Inserts a new container into the database
	 *
	 * @param nameOfContainer
	 */
	public void putContainer(String nameOfContainer) {

		Connection conn = init();
		try {
			Statement s = conn.createStatement();
			s.execute("INSERT into container (container_name) VALUES('" + nameOfContainer + "')");

			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Returns a list of the containers currently stored in the database
	 *
	 * @return A list of container names. The caller method will create the
	 *         containers
	 */
	public List<String> retrieveContainers() {

		Connection conn = init();
		try {
			Statement s = conn.createStatement();
			ResultSet result = s.executeQuery("Select * from container");
			List<String> l = new ArrayList<String>();

			while (result.next()) {
				l.add(result.getString("container_name"));
			}
			return l;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * Verifies if the container is in the database
	 *
	 * @param name The name of the database to be found.
	 * @return True or false depending on if the container is in the database.
	 */
	public boolean findContainer(String name) {
		Connection conn = init();

		try {
			Statement s = conn.createStatement();
			ResultSet result = s
					.executeQuery("select container_name from container WHERE container_name = '" + name + "'");

			Boolean b = result.next();
			conn.close();
			return b;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * Removes container from the database
	 *
	 * @param name The name of the database to be removed.
	 */
	public void removeContainer(String name) {
		Connection conn = init();

		try {
			Statement s = conn.createStatement();
			s.execute("DELETE from container WHERE container_name = '" + name + "'");

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Updates the name of a specific container
	 *
	 * @param prevName The previous name of the container
	 * @param newName  The new name of the container
	 */
	public void editContainer(String prevName, String newName) {

		Connection conn = init();

		try {
			Statement s = conn.createStatement();
			s.execute("UPDATE container SET container_name = '" + newName + "' WHERE container_name = '" + prevName
					+ "'");

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Removes all the items from a container in the database
	 *
	 * @param c the container whose items will be removed from
	 */
	public void emptyContainer(Container c) {

		Connection conn = init();

		try {
			Statement s = conn.createStatement();
			s.execute(String.format("DELETE FROM item WHERE container='%s'", c.getName()));
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a new container to the database.
	 *
	 * @param containerName The name of the container to add.
	 * @param c             The {@link Container} object to be added.
	 */
	public void addContainer(String containerName, Container c) {
		this.putContainer(containerName);

	}

	/**
	 * Adds an item to a specific container.
	 *
	 * @param c    The container to which the item will be added.
	 * @param name The name of the item.
	 * @param ite  The {@link Item} object to be added.
	 */
	public Boolean addItem(Container c, String name, Item ite) {

		Connection conn = init();

		if (this.getItem(c, name) != null) {
			return false;
		}

		try {
			Statement s = conn.createStatement();
			s.execute("INSERT INTO item(name, container, quantity, expiry) VALUES('" + name + "', " + "'" + c.getName()
			+ "', " + "" + ite.getQuantity() + ", '" + "" + ite.getExpiryDate() + "')");
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Removes an item from a specified container.
	 *
	 * @param container    The container from which the item will be removed.
	 * @param itemName The name of the item to be removed.
	 */
	public void removeItem(Container container, String itemName) {

		Connection conn = init();

		try {
			Statement s = conn.createStatement();
			s.execute(String.format("DELETE FROM item WHERE name='%s' AND container='%s'", itemName, container.getName()));
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves an {@link Item} by its container and name.
	 *
	 * @param c        The container in which the item is stored.
	 * @param itemName The name of the item to retrieve.
	 * @return The {@link Item} object if found, {@code null} otherwise.
	 */
	public Item getItem(Container c, String itemName) {
		Connection conn = init();

		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(
					String.format("SELECT * FROM item WHERE name='%s' AND container='%s'", itemName, c.getName()));

			if (rs.next()) {
				GenericTag<FoodGroup> fg = (rs.getString("fg") != null)
						? GenericTag.fromString(FoodGroup.class, rs.getString("fg"))
								: null;
				GenericTag<FoodFreshness> fresh = (rs.getString("fresh") != null)
						? GenericTag.fromString(FoodFreshness.class, rs.getString("fresh"))
								: null;

				conn.close();
				return Item.getInstance(rs.getString("name"), fg, fresh, rs.getInt("quantity"), rs.getDate("expiry"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * Updates the food group of a specific item within a container. If the new
	 * values for food group are provided (not null), the corresponding field of the
	 * item are updated in the database. If value is null, no update is made.
	 *
	 * @param c            The container that contains the item to be updated.
	 * @param itemName     The name of the item to be updated.
	 * @param newFoodGroup The new food group classification for the item (can be
	 *                     null if not updating).
	 */
	public void updateItemFoodGroup(Container c, String itemName, FoodGroup newFoodGroup) {
		List<String> setClauses = new ArrayList<>();
		if (newFoodGroup != null) {
			setClauses.add("fg = '" + newFoodGroup.getDisplayName() + "'");
		}

		if (setClauses.isEmpty()) {
			// If there are no updates to make, simply return
			return;
		}

		String setClause = String.join(", ", setClauses);
		String sql = "UPDATE item SET " + setClause + " WHERE name = ? AND container = ?";

		try (Connection conn = this.init(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, itemName);
			pstmt.setString(2, c.getName());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a list of Items belonging to a Container in the database
	 *
	 * @param c a Container object to retrieve items from
	 * @return a list of Items belonging to a Container in the database
	 */
	public List<Item> retrieveItems(Container c) {
		Connection conn = init();
		try {
			Statement s = conn.createStatement();
			ResultSet result = s.executeQuery(String.format("SELECT * FROM item WHERE container='%s'", c.getName()));
			List<Item> l = new ArrayList<Item>();

			while (result.next()) {
				l.add(this.getItem(new Container(result.getString("container")), result.getString("name")));
			}
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * Updates the freshness status of all items within a specified container in the
	 * database. This method sets the freshness status based on the current date and
	 * the expiry date of the items. Items past the current date are marked as
	 * 'Expired', items expiring within the next 7 days are marked as 'Near_Expiry',
	 * and all other items are marked as 'Fresh'.
	 *
	 * @param container The container whose items' freshness statuses are to be
	 *                  updated.
	 */
	public void batchUpdateItemFreshness(Container container) {
		// SQL query to update the freshness of items based on their expiry date
		String sql = "UPDATE item SET fresh = CASE " + "WHEN expiry < CURRENT_DATE THEN 'Expired'::Freshness "
				+ "WHEN expiry >= CURRENT_DATE AND expiry <= CURRENT_DATE + interval '7' day THEN 'Near_Expiry'::Freshness "
				+ "ELSE 'Fresh'::Freshness END " + "WHERE container = ?";

		try (Connection conn = this.init(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, container.getName());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getStorageTip(String name) {
		Connection conn = init();
		String tip = null;

		try {
			Statement s = conn.createStatement();
			ResultSet result = s.executeQuery(String.format("SELECT * FROM storage_tips WHERE name='%s'", name));

			while (result.next()) {
				tip = result.getString("info");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tip;
	}

	/**
	 * Adds an item to the grocery list in the database.
	 *
	 * @param itemName The name of the item to add to the grocery list.
	 */
	public void addToGroceryList(String itemName) {
		// Establish a connection to the database
		Connection conn = init();
		try {
			// Prepare an SQL statement to insert an item into the grocery table
			PreparedStatement statement = conn.prepareStatement("INSERT INTO grocery (name) VALUES (?)");
			// Set the item name as a parameter in the SQL statement
			statement.setString(1, itemName);
			// Execute the SQL statement to insert the item into the grocery table
			statement.executeUpdate();
			// Close the prepared statement
			statement.close();
			// Close the database connection
			conn.close();
		} catch (SQLException e) {
			// Handle any SQL exceptions by printing the stack trace
			e.printStackTrace();
		}
	}

	/**
	 * Removes an item from the grocery list in the database.
	 *
	 * @param itemName The name of the item to remove from the grocery list.
	 */
	public void removeFromGroceryList(String itemName) {
		// Establish a connection to the database
		Connection conn = init();
		try {
			// Prepare an SQL statement to delete an item from the grocery table based on
			// its name
			PreparedStatement statement = conn.prepareStatement("DELETE FROM grocery WHERE name = ?");
			// Set the item name as a parameter in the SQL statement
			statement.setString(1, itemName);
			// Execute the SQL statement to delete the item from the grocery table
			statement.executeUpdate();
			// Close the prepared statement
			statement.close();
			// Close the database connection
			conn.close();
		} catch (SQLException e) {
			// Handle any SQL exceptions by printing the stack trace
			e.printStackTrace();
		}
	}



	/**
	 * Retrieves all grocery items from the database.
	 *
	 * @return A 2D array containing all grocery items, where each row represents an item.
	 */
	public Object[][] getAllGroceryItems() {
		Connection conn = init();
		List<Object[]> itemList = new ArrayList<>();

		if (conn != null) {
			try {
				PreparedStatement statement = conn.prepareStatement("SELECT * FROM grocery");
				ResultSet resultSet = statement.executeQuery();

				while (resultSet.next()) {
					String itemName = resultSet.getString("name");

					// Create an array representing the current item
					Object[] itemData = { itemName };
					itemList.add(itemData);
				}

				resultSet.close();
				statement.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// Convert the list to a 2D array
		Object[][] itemArray = new Object[itemList.size()][];
		itemList.toArray(itemArray);

		return itemArray;
	}

	/**
	 * Updates the quantity of an item in the database
	 * 
	 * @param item  The name of the item
	 * @param value The quantity to be updated
	 * @param c     The container in which the item belongs to
	 */
	public void updateQuantity(String item, int value, Container c) {

		Connection conn = init();

		try {
			String query = "UPDATE item SET quantity = ? WHERE name = ? AND container = ?";
			PreparedStatement p = conn.prepareStatement(query);
			p.setInt(1, value);
			p.setString(2, item);
			p.setString(3, c.getName());
			p.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}
	/**
	 * Retrieves items that are close to expiring.
	 *
	 * @return A list of item names that are expiring soon.
	 */
	public List<String> getExpiringItems() {
		List<String> expiringItems = new ArrayList<>();
		Connection conn = init();

		try {
			//  select items whose expiry date is within the next 7 days
			String sql = "SELECT name, container FROM item WHERE fresh = 'Near_Expiry'";
			PreparedStatement pstmt = conn.prepareStatement(sql);

			// Execute the query
			ResultSet rs = pstmt.executeQuery();
			//
			//Process the result set
			while (rs.next()) {
				String itemName = rs.getString("name");
				expiringItems.add(itemName + " - " + rs.getString("container"));
			}
			//            while (rs.next()) {
			//                String itemName = rs.getString("name");
			//                String containerName = rs.getString("container");
			//                Date expiryDate = rs.getDate("expiry");
			//                expiringItems.add(itemName + " in " + containerName + " (Expiry: " + expiryDate + ")");
			//            }
			//
			// Close resources
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return expiringItems;
	}

	/**
	 * Retrieves the names of all items that are either Near Expiry or Fresh.
	 *
	 * @return A Set of Strings representing the names of items that are either near expiry or fresh.
	 */
	public Set<String> getNearExpiryOrFreshItemNames() {
		Set<String> itemNames = new HashSet<>();
		Connection conn = init();
		if (conn != null) {
			try {
				String sql = "SELECT name FROM item WHERE fresh IN ('Near_Expiry', 'Fresh')";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					String name = rs.getString("name").toLowerCase();
					itemNames.add(name);
				}

				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return itemNames;
	}


	/**
	 * Retrieves settings for the program
	 * 
	 * @return a array representing the settings for the program
	 */
	public String[] getSettings(){
		String [] settings = new String [2];
		Connection conn = init();
		if (conn != null) {
			try {
				String sql = "SELECT fontsize, notificationBoolean FROM settings";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();

				while(rs.next()) {
					String font = rs.getString("fontsize").toLowerCase();
					String notifBool = rs.getString("notificationBoolean").toLowerCase();

					settings[0] = font;
					settings[1] = notifBool;
				}


				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return settings;
	}
	
	/**
	 * Updates the fontSize of font in the database
	 * @param n The fontSize 
	 */
	public void setFontsize(int n) {
		Connection conn = init();
		try {
			String query = "UPDATE settings SET fontsize = ? WHERE setting_type = 'User'";
			PreparedStatement p = conn.prepareStatement(query);
			p.setInt(1, n);
			p.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets whether or not opening the application will give the notifications and saves it in database
	 * 
	 * @param b Boolean value, true if notifcation should be on, false otherwise
	 */
	public void setNotificationBoolean(boolean b) {
		Connection conn = init();
		try {
			String query = "UPDATE settings SET notificationboolean = ? WHERE setting_type = 'User';";
			PreparedStatement p = conn.prepareStatement(query);
			p.setString(1, Boolean.toString(b));
			p.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void saveRecipeToDatabase(Recipe recipe) {
		Connection conn = init();
		if (conn != null) {
			try {
				conn.setAutoCommit(false); // Disable auto-commit to manage transactions manually

				String insertRecipeSQL = "INSERT INTO recipes (id, title, image_url) VALUES (?, ?, ?)";
				try (PreparedStatement pstmt = conn.prepareStatement(insertRecipeSQL)) {
					pstmt.setInt(1, recipe.getId());
					pstmt.setString(2, recipe.getTitle());
					pstmt.setString(3, recipe.getImage());

					// Execute the update without expecting a returning id
					pstmt.executeUpdate();
				}

				// Use recipe.getId() directly as recipeId is now directly provided by Recipe class
				for (Ingredient ingredient : recipe.getUsedIngredients()) {
					int ingredientId = getOrInsertIngredient(conn, ingredient);
					linkRecipeIngredient(conn, recipe.getId(), ingredientId, ingredient.getAmount(), true);
				}
				for (Ingredient ingredient : recipe.getMissedIngredients()) {
					int ingredientId = getOrInsertIngredient(conn, ingredient);
					linkRecipeIngredient(conn, recipe.getId(), ingredientId, ingredient.getAmount(), false);
				}

				// Insert Detailed Instructions
				String insertInstructionSQL = "INSERT INTO detailed_instructions (recipe_id, step_number, instruction) VALUES (?, ?, ?)";
				try (PreparedStatement pstmt = conn.prepareStatement(insertInstructionSQL)) {
					for (Map.Entry<Integer, String> entry : recipe.getDetailedInstructions().entrySet()) {
						pstmt.setInt(1, recipe.getId()); // Direct use of recipe.getId()
						pstmt.setInt(2, entry.getKey());
						pstmt.setString(3, entry.getValue());
						pstmt.executeUpdate();
					}
				}

				conn.commit(); // Commit transaction
			} catch (SQLException e) {
				// Handle exceptions by rolling back the transaction
				try {
					if (conn != null) conn.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
				e.printStackTrace();
			} finally {
				// Ensure the connection is closed properly
				try {
					if (conn != null) {
						conn.setAutoCommit(true); // Re-enable autoCommit
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private int getOrInsertIngredient(Connection conn, Ingredient ingredient) throws SQLException {
		// Check if the ingredient exists by ID
		String checkIngredientSQL = "SELECT id FROM ingredients WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(checkIngredientSQL)) {
			pstmt.setInt(1, ingredient.getId());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return ingredient.getId(); // Ingredient exists, return its ID
			}
		}

		// Ingredient does not exist, insert it with the provided ID
		String insertIngredientSQL = "INSERT INTO ingredients (id, name, unit, image_url, original) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(insertIngredientSQL)) {
			pstmt.setInt(1, ingredient.getId());
			pstmt.setString(2, ingredient.getName());
			pstmt.setString(3, ingredient.getUnit());
			pstmt.setString(4, ingredient.getImage());
			pstmt.setString(5, ingredient.getOriginal());
			pstmt.executeUpdate();
		}
		return ingredient.getId(); // Return the provided ingredient's ID
	}

	private void linkRecipeIngredient(Connection conn, int recipeId, int ingredientId, double amount, boolean isUsed) throws SQLException {
		// Insert into recipe_ingredients table
		String sql = "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, amount, is_used) VALUES (?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, recipeId);
			pstmt.setInt(2, ingredientId);
			pstmt.setDouble(3, amount);
			pstmt.setBoolean(4, isUsed);
			pstmt.executeUpdate();
		}
	}

	public boolean isRecipeInDatabase(int recipeId) {
		Connection conn = init();
		if (conn != null) {
			try {
				String checkRecipeSQL = "SELECT COUNT(*) FROM recipes WHERE id = ?";
				try (PreparedStatement pstmt = conn.prepareStatement(checkRecipeSQL)) {
					pstmt.setInt(1, recipeId);
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) {
						int count = rs.getInt(1);
						return count > 0; // Return true if the recipe exists, false otherwise
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false; // Return false if the recipe is not found or if any exception occurs
	}

	public List<Recipe> getAllStarredRecipes() {
		List<Recipe> recipes = new ArrayList<>();
		Connection conn = init();
		if (conn != null) {
			try {
				List<Integer> recipeIds = new ArrayList<>();
				// Retrieve all recipes
				String selectRecipesSQL = "SELECT * FROM recipes";
				try (Statement stmt = conn.createStatement();
					 ResultSet rsRecipes = stmt.executeQuery(selectRecipesSQL)) {

					while (rsRecipes.next()) {
						int recipeId = rsRecipes.getInt("id");
						String title = rsRecipes.getString("title");
						String imageUrl = rsRecipes.getString("image_url");

						Recipe recipe = new Recipe(recipeId, title, imageUrl);
						recipe.setFetchedStep(true);
						recipes.add(recipe);
						recipeIds.add(recipeId); // Collect recipe IDs for bulk ingredient and instruction retrieval
					}
				}

				// Bulk fetch ingredients and instructions
				Map<Integer, List<Ingredient>> ingredientsMap = getAllIngredientsForRecipes(conn, recipeIds);
				Map<Integer, Map<Integer, String>> instructionsMap = getAllDetailedInstructionsForRecipes(conn, recipeIds);

				// Associate fetched data with recipes
				for (Recipe recipe : recipes) {
					recipe.setUsedIngredients(ingredientsMap.getOrDefault(recipe.getId(), new ArrayList<>()));
					recipe.setMissedIngredients(new ArrayList<>()); // Assuming you need to adjust how to differentiate used/missed
					recipe.setDetailedInstructions(instructionsMap.getOrDefault(recipe.getId(), new HashMap<>()));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return recipes;
	}

	private Map<Integer, List<Ingredient>> getAllIngredientsForRecipes(Connection conn, List<Integer> recipeIds) throws SQLException {
		Map<Integer, List<Ingredient>> recipeIngredientsMap = new HashMap<>();
		if (recipeIds.isEmpty()) {
			return recipeIngredientsMap;
		}

		String inClause = recipeIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		String selectIngredientsSQL =
				"SELECT ri.recipe_id, i.id, i.name, ri.amount, i.unit, i.image_url, i.original " +
						"FROM ingredients i " +
						"JOIN recipe_ingredients ri ON i.id = ri.ingredient_id " +
						"WHERE ri.recipe_id IN (" + inClause + ")";

		try (Statement stmt = conn.createStatement();
			 ResultSet rsIngredients = stmt.executeQuery(selectIngredientsSQL)) {
			while (rsIngredients.next()) {
				int recipeId = rsIngredients.getInt("recipe_id");
				int id = rsIngredients.getInt("id");
				String name = rsIngredients.getString("name");
				double amount = rsIngredients.getDouble("amount");
				String unit = rsIngredients.getString("unit");
				String image = rsIngredients.getString("image_url");
				String original = rsIngredients.getString("original");

				Ingredient ingredient = new Ingredient(id, name, amount, unit, image, original);
				recipeIngredientsMap.computeIfAbsent(recipeId, k -> new ArrayList<>()).add(ingredient);
			}
		}
		return recipeIngredientsMap;
	}
	private Map<Integer, Map<Integer, String>> getAllDetailedInstructionsForRecipes(Connection conn, List<Integer> recipeIds) throws SQLException {
		Map<Integer, Map<Integer, String>> recipeInstructionsMap = new HashMap<>();
		if (recipeIds.isEmpty()) {
			return recipeInstructionsMap;
		}

		String inClause = recipeIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		String selectInstructionsSQL =
				"SELECT recipe_id, step_number, instruction " +
						"FROM detailed_instructions WHERE recipe_id IN (" + inClause + ") " +
						"ORDER BY recipe_id, step_number";

		try (Statement stmt = conn.createStatement();
			 ResultSet rsInstructions = stmt.executeQuery(selectInstructionsSQL)) {
			while (rsInstructions.next()) {
				int recipeId = rsInstructions.getInt("recipe_id");
				int stepNumber = rsInstructions.getInt("step_number");
				String instruction = rsInstructions.getString("instruction");

				recipeInstructionsMap.computeIfAbsent(recipeId, k -> new HashMap<>()).put(stepNumber, instruction);
			}
		}
		return recipeInstructionsMap;
	}

	public void removeStarredRecipe(Recipe recipe) {
		Connection conn = init();
		if (conn != null) {
			try {
				conn.setAutoCommit(false); // Disable auto-commit to manage transactions manually

				String deleteRecipeSQL = "DELETE FROM recipes WHERE id = ?";
				try (PreparedStatement pstmt = conn.prepareStatement(deleteRecipeSQL)) {
					pstmt.setInt(1, recipe.getId());
					pstmt.executeUpdate();
				}

				String deleteRecipeIngredientsSQL = "DELETE FROM recipe_ingredients WHERE recipe_id = ?";
				try (PreparedStatement pstmt = conn.prepareStatement(deleteRecipeIngredientsSQL)) {
					pstmt.setInt(1, recipe.getId());
					pstmt.executeUpdate();
				}

				String deleteInstructionsSQL = "DELETE FROM detailed_instructions WHERE recipe_id = ?";
				try (PreparedStatement pstmt = conn.prepareStatement(deleteInstructionsSQL)) {
					pstmt.setInt(1, recipe.getId());
					pstmt.executeUpdate();
				}

				conn.commit();
			} catch (SQLException e) {
				try {
					if (conn != null) conn.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.setAutoCommit(true);
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ArrayList<String> getTotalCount(String container) {
		
		Connection conn = init();
		
		try {
			Statement s = conn.createStatement();
			ResultSet result;
			if (container == null){
				result = s.executeQuery("SELECT * FROM item");
			}
			else {
				result = s.executeQuery("Select * FROM item where container='" + container +"'");
			}
			
			ArrayList<String> l = new ArrayList<String>();
			while (result.next()) {
				if (result.getString("fg") != null) {
							l.add(result.getString("fg"));
				}
		
			}
			conn.close();
			return l;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}


}



