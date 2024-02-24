package test;

import domain.logic.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");

    Item item1, item2, itemExpired, itemNearExpiry;
    Date date1, date2, expiredDate, nearExpiryDate;

    @BeforeEach
    void init() throws ParseException {
        date1 = sdf.parse("5-May-2024");
        date2 = sdf.parse("3-January-2024");
        expiredDate = sdf.parse("1-January-2023");
        nearExpiryDate = sdf.parse("26-February-2024");

        Set<GenericTag<FoodGroup>> grain = new HashSet<>();
        grain.add(new GenericTag<>(FoodGroup.GRAIN));
        Set<GenericTag<FoodGroup>> fruit = new HashSet<>();
        fruit.add(new GenericTag<>(FoodGroup.FRUIT));
        GenericTag<FoodFreshness> fresh = new GenericTag<>(FoodFreshness.FRESH);
        GenericTag<FoodFreshness> expired = new GenericTag<>(FoodFreshness.EXPIRED);
        GenericTag<FoodFreshness> nearExpiry = new GenericTag<>(FoodFreshness.NEAR_EXPIRY);

        item1 = Item.getInstance("Bread", grain, fresh, 3, date1);
        item2 = Item.getInstance("Apple", fruit, expired, 2, date2);
        itemExpired = Item.getInstance("Expired Milk", new HashSet<>(), expired, 1, expiredDate);
        itemNearExpiry = Item.getInstance("Chicken salad", new HashSet<>(), nearExpiry, 1, nearExpiryDate);
    }

    @Test
    void testItemAttributes() {
        assertAll(
                () -> assertEquals("Bread", item1.getName()),
                () -> assertEquals(3, item1.getQuantity()),
                () -> assertEquals(date1, item1.getExpiryDate()),
                () -> assertEquals(FoodFreshness.FRESH, item1.getFoodFreshnessTag().getTag())
        );
    }

    @Test
    void testEqualityAndHashCode() {
        Item item1Copy = Item.getInstance("Bread", new HashSet<>(), new GenericTag<>(FoodFreshness.FRESH), 3, date1);
        assertEquals(item1, item1Copy);
        assertEquals(item1.hashCode(), item1Copy.hashCode());
    }

    @Test
    void testCompareTo() {
        assertTrue(item1.compareTo(item2) > 0); // item1 expires later than item2
        assertTrue(itemExpired.compareTo(itemNearExpiry) < 0); // itemExpired expires before itemNearExpiry
    }

    @Test
    void testSettersAndGetters() {
        item1.setName("New Bread");
        item1.setQuantity(5);
        Date newDate = new Date();
        item1.setExpiryDate(newDate);

        assertAll(
                () -> assertEquals("New Bread", item1.getName()),
                () -> assertEquals(5, item1.getQuantity()),
                () -> assertEquals(newDate, item1.getExpiryDate())
        );
    }

    @Test
    void testInvalidQuantity() {
        assertThrows(IllegalArgumentException.class, () -> Item.getInstance("Error Item", -1));
    }

    @Test
    void testInvalidDateFormat() {
        assertThrows(RuntimeException.class, () -> Item.getInstance("Bad Date Item", new HashSet<>(), new GenericTag<>(FoodFreshness.FRESH), 1, "31-02-2024"));
    }


    @Test
    void testToString() throws ParseException {
        HashSet<GenericTag<FoodGroup>> foodGroupTags = new HashSet<>();
        foodGroupTags.add(new GenericTag<>(FoodGroup.DAIRY));
        Date date = new SimpleDateFormat("dd-MMMM-yyyy").parse("9-jan-2024");

        Item item = Item.getInstance("Test Item", foodGroupTags, new GenericTag<>(FoodFreshness.FRESH), 1, date);
        String expected = "Item{name='Test Item', foodGroupTags=[dairy], foodFreshnessTag=fresh, quantity=1, expiryDate=Tue Jan 09 00:00:00 EST 2024}";
        assertEquals(expected, item.toString(), "toString does not format item as expected.");
    }

    @Test
    void testEdgeCaseDateLeapYear() throws ParseException {
        Date leapYearDate = new SimpleDateFormat("dd-MMMM-yyyy").parse("29-February-2024");
        HashSet<GenericTag<FoodGroup>> foodGroupTags = new HashSet<>();
        Item leapYearItem = Item.getInstance("Leap Year Item", foodGroupTags, new GenericTag<>(FoodFreshness.FRESH), 1, leapYearDate);
        assertEquals(leapYearDate, leapYearItem.getExpiryDate(), "Leap year date is not set correctly.");
    }


}
