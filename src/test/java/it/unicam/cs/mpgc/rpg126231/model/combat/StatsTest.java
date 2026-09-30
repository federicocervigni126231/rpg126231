package it.unicam.cs.mpgc.rpg126231.model.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StatsTest {

    @Test
    void plusSumsEveryValue() {
        assertEquals(new Stats(15, 7, 4), new Stats(10, 5, 3).plus(new Stats(5, 2, 1)));
    }

    @Test
    void timesMultipliesEveryValue() {
        assertEquals(new Stats(30, 6, 3), new Stats(10, 2, 1).times(3));
    }

    @Test
    void timesZeroGivesZeroStats() {
        assertEquals(new Stats(0, 0, 0), new Stats(10, 2, 1).times(0));
    }

    @Test
    void negativeValuesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Stats(-1, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> new Stats(0, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> new Stats(0, 0, -1));
    }
}
