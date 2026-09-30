package it.unicam.cs.mpgc.rpg126231.model.enemy;

import it.unicam.cs.mpgc.rpg126231.model.combat.HitModifier;
import it.unicam.cs.mpgc.rpg126231.model.combat.RecordingDamageResolver;
import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;
import it.unicam.cs.mpgc.rpg126231.model.hero.Hero;
import it.unicam.cs.mpgc.rpg126231.model.hero.Warrior;
import it.unicam.cs.mpgc.rpg126231.model.item.Item;
import it.unicam.cs.mpgc.rpg126231.model.item.Potion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnemyTest {

    private static final Stats ORC_STATS = new Stats(100, 12, 4);

    private Hero hero;
    private RecordingDamageResolver resolver;

    @BeforeEach
    void setUp() {
        hero = new Hero("Aldo", new Warrior());
        resolver = new RecordingDamageResolver(5);
    }

    @Test
    void aggressiveEnemyAlwaysHitsNormally() {
        Enemy goblin = new Enemy("Goblin", ORC_STATS, 20, new AggressiveBehavior(), List.of());
        goblin.takeDamage(95);
        goblin.takeTurn(hero, resolver);

        assertEquals(1, resolver.hits().size());
        assertSame(goblin, resolver.hits().get(0).attacker());
        assertSame(hero, resolver.hits().get(0).defender());
        assertEquals(HitModifier.NORMAL, resolver.hits().get(0).modifier());
    }

    @Test
    void berserkerHitsNormallyAboveThreshold() {
        Enemy orc = new Enemy("Orco", ORC_STATS, 50, new BerserkerBehavior(), List.of());
        orc.takeDamage(69);
        orc.takeTurn(hero, resolver);

        assertEquals(HitModifier.NORMAL, resolver.hits().get(0).modifier());
    }

    @Test
    void berserkerHitsHarderAtOrBelowThreshold() {
        Enemy orc = new Enemy("Orco", ORC_STATS, 50, new BerserkerBehavior(), List.of());
        orc.takeDamage(70);
        orc.takeTurn(hero, resolver);

        assertEquals(1.5, resolver.hits().get(0).modifier().multiplier());
    }

    @Test
    void templateSpawnsFreshEnemiesWithFullHp() {
        List<Item> drops = List.of(new Potion("small_potion", "Pozione piccola", 30));
        EnemyTemplate template = new EnemyTemplate("Orco", ORC_STATS, 50, new BerserkerBehavior(), drops);

        Enemy first = template.spawn();
        first.takeDamage(30);
        Enemy second = template.spawn();

        assertNotSame(first, second);
        assertEquals(ORC_STATS.maxHp(), second.currentHp());
        assertEquals(50, second.experienceReward());
        assertEquals(drops, second.possibleDrops());
    }

    @Test
    void invalidEnemiesAreRejected() {
        AggressiveBehavior behavior = new AggressiveBehavior();
        assertThrows(IllegalArgumentException.class,
                () -> new Enemy("Ombra", new Stats(0, 5, 5), 10, behavior, List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> new Enemy("Goblin", ORC_STATS, -1, behavior, List.of()));
    }
}
