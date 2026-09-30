package it.unicam.cs.mpgc.rpg126231.model.combat;

import it.unicam.cs.mpgc.rpg126231.model.enemy.AggressiveBehavior;
import it.unicam.cs.mpgc.rpg126231.model.enemy.Enemy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CombatantTest {

    private Combatant combatant;

    @BeforeEach
    void setUp() {
        combatant = new Enemy("Goblin", new Stats(50, 8, 2), 20, new AggressiveBehavior(), List.of());
    }

    @Test
    void startsAliveWithFullHp() {
        assertEquals(50, combatant.currentHp());
        assertTrue(combatant.isAlive());
    }

    @Test
    void takeDamageReducesHp() {
        assertEquals(20, combatant.takeDamage(20));
        assertEquals(30, combatant.currentHp());
    }

    @Test
    void takeDamageNeverGoesBelowZero() {
        assertEquals(50, combatant.takeDamage(80));
        assertEquals(0, combatant.currentHp());
        assertFalse(combatant.isAlive());
    }

    @Test
    void healNeverGoesAboveMax() {
        combatant.takeDamage(10);
        assertEquals(10, combatant.heal(30));
        assertEquals(50, combatant.currentHp());
    }

    @Test
    void negativeAmountsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> combatant.takeDamage(-1));
        assertThrows(IllegalArgumentException.class, () -> combatant.heal(-1));
    }

    @Test
    void blankNameIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Enemy(" ", new Stats(10, 1, 1), 0, new AggressiveBehavior(), List.of()));
    }
}
