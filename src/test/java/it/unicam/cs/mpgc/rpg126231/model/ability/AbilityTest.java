package it.unicam.cs.mpgc.rpg126231.model.ability;

import it.unicam.cs.mpgc.rpg126231.model.combat.HitModifier;
import it.unicam.cs.mpgc.rpg126231.model.combat.RecordingDamageResolver;
import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;
import it.unicam.cs.mpgc.rpg126231.model.enemy.AggressiveBehavior;
import it.unicam.cs.mpgc.rpg126231.model.enemy.Enemy;
import it.unicam.cs.mpgc.rpg126231.model.hero.Hero;
import it.unicam.cs.mpgc.rpg126231.model.hero.Rogue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbilityTest {

    private Hero user;
    private Enemy target;

    @BeforeEach
    void setUp() {
        user = new Hero("Aldo", new Rogue());
        target = new Enemy("Orco", new Stats(100, 10, 5), 50, new AggressiveBehavior(), List.of());
    }

    @Test
    void powerStrikeHitsOnceWithDoublePower() {
        RecordingDamageResolver resolver = new RecordingDamageResolver(10);
        new PowerStrike().use(user, target, resolver);

        assertEquals(1, resolver.hits().size());
        RecordingDamageResolver.Hit hit = resolver.hits().get(0);
        assertSame(user, hit.attacker());
        assertSame(target, hit.defender());
        assertEquals(new HitModifier(2.0, false), hit.modifier());
    }

    @Test
    void fireballIgnoresDefense() {
        RecordingDamageResolver resolver = new RecordingDamageResolver(10);
        new Fireball().use(user, target, resolver);

        assertEquals(1, resolver.hits().size());
        assertTrue(resolver.hits().get(0).modifier().ignoresDefense());
    }

    @Test
    void doubleStrikeHitsTwiceWhenTargetSurvivesTheFirstHit() {
        RecordingDamageResolver resolver = new RecordingDamageResolver(10);
        new DoubleStrike().use(user, target, resolver);

        assertEquals(2, resolver.hits().size());
        assertEquals(80, target.currentHp());
    }

    @Test
    void doubleStrikeStopsWhenTargetDiesOnTheFirstHit() {
        RecordingDamageResolver resolver = new RecordingDamageResolver(200);
        new DoubleStrike().use(user, target, resolver);

        assertEquals(1, resolver.hits().size());
        assertFalse(target.isAlive());
    }
}
