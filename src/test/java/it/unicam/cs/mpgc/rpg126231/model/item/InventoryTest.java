package it.unicam.cs.mpgc.rpg126231.model.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryTest {

    private static final Potion POTION = new Potion("small_potion", "Pozione piccola", 30);
    private static final Weapon SWORD = new Weapon("sword", "Spada", 5);

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

    @Test
    void addedItemsKeepInsertionOrder() {
        inventory.add(POTION);
        inventory.add(SWORD);
        assertEquals(List.of(POTION, SWORD), inventory.items());
    }

    @Test
    void removeTakesOnlyOneCopy() {
        inventory.add(POTION);
        inventory.add(POTION);
        inventory.remove(POTION);
        assertEquals(List.of(POTION), inventory.items());
    }

    @Test
    void removingMissingItemIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> inventory.remove(POTION));
    }

    @Test
    void containsReflectsContent() {
        assertFalse(inventory.contains(SWORD));
        inventory.add(SWORD);
        assertTrue(inventory.contains(SWORD));
    }

    @Test
    void itemsOfTypeFiltersByType() {
        inventory.add(POTION);
        inventory.add(SWORD);
        assertEquals(List.of(POTION), inventory.itemsOfType(Consumable.class));
        assertEquals(List.of(SWORD), inventory.itemsOfType(Weapon.class));
    }

    @Test
    void itemsViewCannotBeModified() {
        assertThrows(UnsupportedOperationException.class, () -> inventory.items().add(POTION));
    }
}
