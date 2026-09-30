package it.unicam.cs.mpgc.rpg126231.service;

import it.unicam.cs.mpgc.rpg126231.model.combat.HitModifier;
import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;
import it.unicam.cs.mpgc.rpg126231.model.enemy.AggressiveBehavior;
import it.unicam.cs.mpgc.rpg126231.model.enemy.Enemy;
import it.unicam.cs.mpgc.rpg126231.service.event.EventBus;
import it.unicam.cs.mpgc.rpg126231.service.event.GameEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DamageCalculatorTest {

    /** Generatore che restituisce sempre 0: nessun bonus casuale. */
    private static final RandomGenerator NO_BONUS = () -> 0L;

    private final EventBus events = new EventBus();
    private final List<GameEvent> published = new ArrayList<>();
    private Enemy attacker;
    private Enemy defender;

    @BeforeEach
    void setUp() {
        events.subscribe(published::add);
        attacker = enemy(new Stats(100, 14, 0));
        defender = enemy(new Stats(1000, 0, 4));
    }

    @Test
    void normalHitIsAttackMinusDefense() {
        assertEquals(10, new DamageCalculator(NO_BONUS, events).hit(attacker, defender, HitModifier.NORMAL));
        assertEquals(990, defender.currentHp());
    }

    @Test
    void multiplierScalesAttack() {
        assertEquals(24, new DamageCalculator(NO_BONUS, events).hit(attacker, defender, new HitModifier(2.0, false)));
    }

    @Test
    void ignoringDefenseUsesFullAttack() {
        assertEquals(14, new DamageCalculator(NO_BONUS, events).hit(attacker, defender, new HitModifier(1.0, true)));
    }

    @Test
    void damageIsAtLeastOne() {
        Enemy weak = enemy(new Stats(100, 1, 0));
        Enemy armored = enemy(new Stats(100, 0, 50));
        assertEquals(1, new DamageCalculator(NO_BONUS, events).hit(weak, armored, HitModifier.NORMAL));
    }

    @Test
    void randomBonusStaysBetweenZeroAndTwo() {
        DamageCalculator calculator = new DamageCalculator(new Random(42), events);
        for (int i = 0; i < 100; i++) {
            int dealt = calculator.hit(attacker, enemy(new Stats(1000, 0, 4)), HitModifier.NORMAL);
            assertTrue(dealt >= 10 && dealt <= 12, "danno fuori intervallo: " + dealt);
        }
    }

    @Test
    void publishesTheDamageActuallyDealt() {
        Enemy almostDead = enemy(new Stats(100, 0, 0));
        almostDead.takeDamage(97);

        new DamageCalculator(NO_BONUS, events).hit(attacker, almostDead, HitModifier.NORMAL);

        assertEquals(List.of(new GameEvent.DamageDealt(attacker, almostDead, 3)), published);
    }

    private static Enemy enemy(Stats stats) {
        return new Enemy("Bersaglio", stats, 0, new AggressiveBehavior(), List.of());
    }
}
