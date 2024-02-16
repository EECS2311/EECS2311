package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import domain.logic.FoodGroup;
import domain.logic.FoodFreshness;
import domain.logic.GenericTag;
import domain.logic.TagUtility;
import domain.logic.Tag;


class TagsTest {

	/**
	 * Tests the functionality of the GenericTag and TagUtility class with the FoodGroup enum.
	 */
	@Test
	void FoodGroupTest() {
		FoodGroup g = FoodGroup.GRAIN;
		FoodGroup d = FoodGroup.DAIRY;
		FoodGroup p = FoodGroup.PROTEIN;
		FoodGroup v = FoodGroup.VEGETABLE;
		FoodGroup f = FoodGroup.FRUIT;
		GenericTag<Tag> expected = new GenericTag<Tag>(g);
		GenericTag<Tag> expected2 = new GenericTag<Tag>(d);
		
		Assertions.assertEquals(expected, GenericTag.fromString(FoodGroup.class, "grain"));
		Assertions.assertNotEquals(expected2, GenericTag.fromString(FoodGroup.class, "grAin"));
		Assertions.assertEquals(expected, GenericTag.fromString(FoodGroup.class, "GRAIN"));
		Assertions.assertEquals(expected2, GenericTag.fromString(FoodGroup.class, "dairy"));
		
		Assertions.assertNotEquals(expected, GenericTag.fromString(FoodGroup.class, "dairy"));
		assertThrows(IllegalArgumentException.class, () -> GenericTag.fromString(FoodGroup.class, "expired"));
		
		List<FoodGroup> groupList = new ArrayList<FoodGroup>();
		groupList.add(g);
		groupList.add(p);
		groupList.add(v);
		groupList.add(f);
		groupList.add(d);
		
		assertIterableEquals(groupList, TagUtility.getAllEnumValues(FoodGroup.class));
		
		List<String> groupNamesList = new ArrayList<String>();
		for (FoodGroup i : groupList) {
			groupNamesList.add(i.getDisplayName());
		}
		assertIterableEquals(groupNamesList, TagUtility.getAllDisplayNames(FoodGroup.class));
		
	}
	
	/**
	 * Tests the functionality of the GenericTag and TagUtility class with the FoodFreshness enum.
	 */
	@Test
	void FoodFreshnessTest() {
		FoodFreshness e = FoodFreshness.EXPIRED;
		FoodFreshness f = FoodFreshness.FRESH;
		FoodFreshness n = FoodFreshness.NEAR_EXPIRY;
		GenericTag<Tag> expected = new GenericTag<Tag>(f);
		GenericTag<Tag> expected2 = new GenericTag<Tag>(e);
		
		Assertions.assertEquals(expected2, GenericTag.fromString(FoodFreshness.class, "expired"));
		Assertions.assertEquals(expected, GenericTag.fromString(FoodFreshness.class, "fresh"));
		Assertions.assertNotEquals(expected, GenericTag.fromString(FoodFreshness.class, "expired"));
		assertThrows(IllegalArgumentException.class, () -> GenericTag.fromString(FoodFreshness.class, "grain"));
		
		List<FoodFreshness> freshnessList = new ArrayList<FoodFreshness>();
		freshnessList.add(e);
		freshnessList.add(f);
		freshnessList.add(n);

		
		assertIterableEquals(freshnessList, TagUtility.getAllEnumValues(FoodFreshness.class));
		
		List<String> freshnessNamesList = new ArrayList<String>();
		for (FoodFreshness i : freshnessList) {
			freshnessNamesList.add(i.getDisplayName());
		}
		assertIterableEquals(freshnessNamesList, TagUtility.getAllDisplayNames(FoodFreshness.class));
		 
	}

}
