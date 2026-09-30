package it.unicam.cs.mpgc.rpg126231.model.dungeon;

import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;
import it.unicam.cs.mpgc.rpg126231.model.enemy.AggressiveBehavior;
import it.unicam.cs.mpgc.rpg126231.model.enemy.EnemyTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DungeonTest {

    private static final EnemyTemplate GOBLIN = template("Goblin");
    private static final EnemyTemplate WOLF = template("Lupo");
    private static final EnemyTemplate DRAGON = template("Drago");

    private Dungeon dungeon;

    @BeforeEach
    void setUp() {
        dungeon = new Dungeon(List.of(new Floor(List.of(GOBLIN, WOLF)), new Floor(List.of(DRAGON))));
    }

    @Test
    void encounterAtReturnsTheEnemyOfThatPosition() {
        assertEquals(GOBLIN, dungeon.encounterAt(DungeonProgress.START));
        assertEquals(WOLF, dungeon.encounterAt(new DungeonProgress(0, 1)));
        assertEquals(DRAGON, dungeon.encounterAt(new DungeonProgress(1, 0)));
    }

    @Test
    void nextMovesToTheNextEncounterOfTheSameFloor() {
        assertEquals(Optional.of(new DungeonProgress(0, 1)), dungeon.next(DungeonProgress.START));
    }

    @Test
    void nextMovesToTheFirstEncounterOfTheNextFloor() {
        assertEquals(Optional.of(new DungeonProgress(1, 0)), dungeon.next(new DungeonProgress(0, 1)));
    }

    @Test
    void nextIsEmptyAfterTheLastEncounter() {
        assertEquals(Optional.empty(), dungeon.next(new DungeonProgress(1, 0)));
    }

    @Test
    void containsRecognisesValidPositions() {
        assertTrue(dungeon.contains(new DungeonProgress(0, 1)));
        assertFalse(dungeon.contains(new DungeonProgress(1, 1)));
        assertFalse(dungeon.contains(new DungeonProgress(2, 0)));
    }

    @Test
    void positionsOutsideTheDungeonAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> dungeon.encounterAt(new DungeonProgress(2, 0)));
        assertThrows(IllegalArgumentException.class, () -> dungeon.next(new DungeonProgress(1, 1)));
        assertThrows(IllegalArgumentException.class, () -> new DungeonProgress(-1, 0));
    }

    @Test
    void emptyStructuresAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Dungeon(List.of()));
        assertThrows(IllegalArgumentException.class, () -> new Floor(List.of()));
    }

    private static EnemyTemplate template(String name) {
        return new EnemyTemplate(name, new Stats(10, 1, 1), 5, new AggressiveBehavior(), List.of());
    }
}
