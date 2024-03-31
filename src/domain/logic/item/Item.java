package domain.logic.item;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Represents an item with a name, quantity, a set of food group tags, a food freshness tag, and an expiration date.
 * This class also implements the Comparable interface to allow items to be sorted based on their expiration date and name.
 */
public class Item implements Comparable<Item>{
    private String name;
    private GenericTag<FoodGroup> foodGroupTag;
    private GenericTag<FoodFreshness> foodFreshnessTag;
    private int quantity;

    private Date expiryDate;

    // Constructors are private to enforce the use of factory methods for instance creation.
    private Item(String name, int quantity, Date expiryDate){
        this(name, quantity);
        this.expiryDate = expiryDate;
    }
    private Item(String name){
        this.name = name;
    }
    private Item(String name, int quantity){
        this(name);
        this.quantity = quantity;
    }

    /**
     * Factory method to create an instance of Item with specified properties.
     *
     * @param name The name of the item.
     * @param foodGroupTag A set of generic tags representing the food groups of the item.
     * @param foodFreshnessTag A generic tag representing the freshness of the item.
     * @param quantity The quantity of the item.
     * @param expiryDate The expiration date of the item.
     * @return An instance of Item.
     */
    public static Item getInstance(String name, GenericTag<FoodGroup> foodGroupTag, GenericTag<FoodFreshness> foodFreshnessTag, int quantity, Date expiryDate){
        Item item = Item.getInstance(name, quantity, expiryDate);
        item.setFoodGroupTag(foodGroupTag);
        item.setFoodFreshnessTag(foodFreshnessTag);
        return item;
    }

    /**
     * Factory method to create an instance of Item with specified properties including a string representation of the expiry date.
     *
     * @param name The name of the item.
     * @param foodGroupTag A set of generic tags representing the food groups of the item.
     * @param foodFreshnessTag A generic tag representing the freshness of the item.
     * @param quantity The quantity of the item.
     * @param expiryDate A string representation of the expiration date in the format "dd-MMMM-yyyy".
     * @return An instance of Item.
     */
    public static Item getInstance(String name, GenericTag<FoodGroup> foodGroupTag, GenericTag<FoodFreshness> foodFreshnessTag, int quantity, String expiryDate){
        Date date = parseDate(expiryDate);
        return Item.getInstance(name, foodGroupTag, foodFreshnessTag, quantity, date);
    }

    /**
     * Factory method to create an Item instance with food groups and freshness defined by enums.
     *
     * @param name The item's name.
     * @param foodGroup A set of FoodGroup enums.
     * @param foodFreshness The FoodFreshness enum value.
     * @param quantity The quantity of the item.
     * @param expiryDate The item's expiry date.
     * @return A new Item instance.
     */
    public static Item getInstance(String name, FoodGroup foodGroup, FoodFreshness foodFreshness, int quantity, Date expiryDate) {
        Item item = Item.getInstance(name, quantity, expiryDate);
        item.setFoodGroupTag(foodGroup);
        item.setFoodFreshnessTag(foodFreshness);
        return item;
    }

    /**
     * Overloaded factory method to create an Item instance with food groups and freshness defined by enums and a string expiry date.
     *
     * @param name The item's name.
     * @param foodGroup A set of FoodGroup enums.
     * @param foodFreshness The FoodFreshness enum value.
     * @param quantity The quantity of the item.
     * @param expiryDate A string representing the item's expiry date in "dd-MMMM-yyyy" format.
     * @return A new Item instance.
     */
    public static Item getInstance(String name, FoodGroup foodGroup, FoodFreshness foodFreshness, int quantity, String expiryDate) {
        Date date = parseDate(expiryDate);
        return Item.getInstance(name, foodGroup, foodFreshness, quantity, date);
    }

    /**
     * Factory method to create an Item instance with a specified name and quantity.
     * Throws IllegalArgumentException if quantity is less than or equal to 0.
     *
     * @param name The item's name.
     * @param quantity The quantity of the item.
     * @return A new Item instance.
     * @throws IllegalArgumentException if quantity is less than or equal to 0.
     */
    public static Item getInstance(String name, int quantity){
        if(quantity <= 0) throw new IllegalArgumentException();
        return new Item(name, quantity);
    }

    /**
     * Factory method to create an Item instance with only a name.
     *
     * @param name The item's name.
     * @return A new Item instance.
     */
    public static Item getInstance(String name){
        return new Item(name);
    }

    /**
     * Factory method to create an Item instance with a name, quantity, and expiry date.
     *
     * @param name The name of the item.
     * @param quantity The quantity of the item.
     * @param expiryDate The expiry date of the item.
     * @return A new Item instance with the specified properties.
     */
    public static Item getInstance(String name, int quantity, Date expiryDate){
        Item temp = Item.getInstance(name, quantity);
        temp.setExpiryDate(expiryDate);
        return temp;
    }

    /**
     * Factory method to create an Item instance with a name, quantity, and expiry date provided as a string.
     *
     * @param name The name of the item.
     * @param quantity The quantity of the item.
     * @param expiryDate The expiry date of the item, in "dd-MMMM-yyyy" format.
     * @return A new Item instance with the specified properties.
     */
    public static Item getInstance(String name, int quantity, String expiryDate){
        Date date = parseDate(expiryDate);
        return Item.getInstance(name, quantity, date);
    }

    /**
     * Factory method to create an Item instance from another Item.
     *
     * @param item The Item object of interest.
     * @return A new Item instance with the same properties as the item parameter.
     */
    public static Item getInstance(Item item){
        return Item.getInstance(item.getName(), item.getFoodGroupTag(), item.getFoodFreshnessTag(), item.getQuantity(), item.getExpiryDate());
    }

    // Helper method to parse date strings
    private static Date parseDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format: " + dateString);
        }
    }

    /**
     * Gets the item's name.
     *
     * @return The item's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the item's name.
     *
     * @param name The new name for the item.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the food group tag.
     *
     * @return The food group tag if it exists, or null otherwise.
     */
    public GenericTag<FoodGroup> getFoodGroupTag() {
        // Directly return null if foodGroupTag is null, avoiding NullPointerException
        if (this.foodGroupTag == null) {
            return null;
        }
        // Otherwise, return a new GenericTag with the existing tag value
        return new GenericTag<>(this.foodGroupTag.getTag());
    }

    /**
     * Sets the food group tag from a GenericTag<FoodGroup>.
     *
     * @param tag The new food group tag.
     */
    public void setFoodGroupTag(GenericTag<FoodGroup> tag) {
        if (tag == null) {
            this.foodGroupTag =  null;
        }
        else{
            this.foodGroupTag = new GenericTag<>(tag);
        }
    }

    /**
     * Sets the food group tag directly from a FoodGroup enum.
     *
     * @param tag The FoodGroup enum value.
     */
    public void setFoodGroupTag(FoodGroup tag) {
        this.foodGroupTag = new GenericTag<>(tag);
    }

    /**
     * Gets the food freshness tag.
     *
     * @return The food freshness tag if it exists, or null otherwise.
     */
    public GenericTag<FoodFreshness> getFoodFreshnessTag() {
        // Directly return null if foodFreshnessTag is null, avoiding NullPointerException
        if (this.foodFreshnessTag == null) {
            return null;
        }
        // Otherwise, return a new GenericTag with the existing tag value
        return new GenericTag<>(this.foodFreshnessTag.getTag());
    }

    /**
     * Sets the food freshness tag from a GenericTag<FoodFreshness>.
     *
     * @param tag The new food freshness tag.
     */
    public void setFoodFreshnessTag(GenericTag<FoodFreshness> tag) {
        if (tag == null) {
            this.foodFreshnessTag =  null;
        }
        else{
            this.foodFreshnessTag = new GenericTag<>(tag);
        }
    }

    /**
     * Sets the food freshness tag directly from a FoodFreshness enum.
     *
     * @param tag The FoodFreshness enum value.
     */
    public void setFoodFreshnessTag(FoodFreshness tag) {
        this.foodFreshnessTag = new GenericTag<>(tag);
    }

    /**
     * Gets the item's quantity.
     *
     * @return The item's quantity.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the item's quantity. Throws IllegalArgumentException if the new quantity is less than or equal to 0.
     *
     * @param quantity The new quantity for the item.
     * @throws IllegalArgumentException if quantity is less than or equal to 0.
     */
    public void setQuantity(int quantity) {
        if(quantity <= 0) throw new IllegalArgumentException();
        this.quantity = quantity;
    }

    /**
     * Gets the item's expiry date.
     *
     * @return A new Date object representing the item's expiry date.
     */
    public Date getExpiryDate() {
        return new Date(expiryDate.getTime());
    }

    /**
     * Sets the item's expiry date from a Date object.
     *
     * @param expiryDate The new expiry date for the item.
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = new Date(expiryDate.getTime());
    }

    /**
     * Sets the item's expiry date from a string in "dd-MMMM-yyyy" format.
     *
     * @param expiryDate The string representing the new expiry date.
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate= parseDate(expiryDate);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * The equality is based on the item's name and expiry date.
     *
     * @param o the reference object with which to compare.
     * @return true if this object is the same as the o argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name) && Objects.equals(expiryDate, item.expiryDate);
    }

    /**
     * Returns a hash code value for the item.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, expiryDate);
    }

    /**
     * Returns a string representation of the item.
     * The string representation includes the item's name, food group tags, food freshness tag, quantity, and expiry date.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", foodGroupTags=" + foodGroupTag +
                ", foodFreshnessTag=" + foodFreshnessTag +
                ", quantity=" + quantity +
                ", expiryDate=" + expiryDate +
                '}';
    }

    /**
     * Compares this item with the specified item for order.
     * Ordering is first done by the expiry date and then by the item's name.
     *
     * @param other the item to be compared.
     * @return a negative integer, zero, or a positive integer as this item is less than, equal to, or greater than the specified item.
     */
    @Override
    public int compareTo(Item other) {
        // Handle null expiryDate for both this item and the other item
        if (this.expiryDate == null && other.expiryDate != null) return -1;
        if (this.expiryDate != null && other.expiryDate == null) return 1;
        if (this.expiryDate == null && other.expiryDate == null) return 0;

        // If neither expiryDate is null, compare them directly
        int dateComparison = this.expiryDate.compareTo(other.expiryDate);
        if (dateComparison != 0) return dateComparison;

        // Compare by name next, handling potential nulls as before
        if (this.name == null && other.name != null) return -1;
        if (this.name != null && other.name == null) return 1;
        if (this.name == null && other.name == null) return 0;

        return this.name.compareTo(other.name);
    }
}
