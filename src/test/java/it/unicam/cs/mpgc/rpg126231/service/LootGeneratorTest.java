package it.unicam.cs.mpgc.rpg126231.service;

import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;
import it.unicam.cs.mpgc.rpg126231.model.enemy.AggressiveBehavior;
import it.unicam.cs.mpgc.rpg126231.model.enemy.Enemy;
import it.unicam.cs.mpgc.rpg126231.model.item.Item;
import it.unicam.cs.mpgc.rpg126231.model.item.Potion;
import it.unicam.cs.mpgc.rpg126231.model.item.Weapon;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LootGeneratorTest {

    private static final RandomGenerator ALWAYS_ZERO = () -> 0L;
    private static final Potion POTION = new Potion("small_potion", "Pozione piccola", 30);
    private static final Weapon SWORD = new Weapon("sword", "Spada", 5);

    @Test
    void certainDropReturnsOneOfThePossibleItems() {
        LootGenerator generator = new LootGenerator(ALWAYS_ZERO, 100);
        assertEquals(Optional.of(POTION), generator.roll(enemyDropping(List.of(POTION, SWORD))));
    }

    @Test
    void zeroChanceNeverDrops() {
        LootGenerator generator = new LootGenerator(ALWAYS_ZERO, 0);
        assertEquals(Optional.empty(), generator.roll(enemyDropping(List.of(POTION))));
    }

    @Test
    void enemyWithoutDropsNeverDrops() {
        LootGenerator generator = new LootGenerator(ALWAYS_ZERO, 100);
        assertEquals(Optional.empty(), generator.roll(enemyDropping(List.of())));
    }

    @Test
    void chanceOutsideRangeIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new LootGenerator(ALWAYS_ZERO, -1));
        assertThrows(IllegalArgumentException.class, () -> new LootGenerator(ALWAYS_ZERO, 101));
    }

    private static Enemy enemyDropping(List<Item> drops) {
        return new Enemy("Goblin", new Stats(10, 1, 0), 10, new AggressiveBehavior(), drops);
    }
}
