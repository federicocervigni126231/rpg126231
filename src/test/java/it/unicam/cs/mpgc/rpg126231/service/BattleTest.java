package it.unicam.cs.mpgc.rpg126231.service;

import it.unicam.cs.mpgc.rpg126231.model.combat.BattleOutcome;
import it.unicam.cs.mpgc.rpg126231.model.combat.RecordingDamageResolver;
import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;
import it.unicam.cs.mpgc.rpg126231.model.enemy.AggressiveBehavior;
import it.unicam.cs.mpgc.rpg126231.model.enemy.Enemy;
import it.unicam.cs.mpgc.rpg126231.model.hero.Hero;
import it.unicam.cs.mpgc.rpg126231.model.hero.Warrior;
import it.unicam.cs.mpgc.rpg126231.model.item.Potion;
import it.unicam.cs.mpgc.rpg126231.service.event.EventBus;
import it.unicam.cs.mpgc.rpg126231.service.event.GameEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BattleTest {

    private static final int DAMAGE_PER_HIT = 10;
    private static final Potion POTION = new Potion("small_potion", "Pozione piccola", 30);

    private final EventBus events = new EventBus();
    private final List<GameEvent> published = new ArrayList<>();
    private Hero hero;

    @BeforeEach
    void setUp() {
        events.subscribe(published::add);
        hero = new Hero("Aldo", new Warrior());
    }

    @Test
    void attackIsFollowedByTheEnemyTurn() {
        Enemy enemy = enemyWithHp(30);
        Battle battle = battleAgainst(enemy);

        battle.heroAttack();

        assertEquals(20, enemy.currentHp());
        assertEquals(hero.maxHp() - DAMAGE_PER_HIT, hero.currentHp());
        assertEquals(BattleOutcome.ONGOING, battle.outcome());
    }

    @Test
    void killingTheEnemyWinsWithoutCounterattack() {
        Enemy enemy = enemyWithHp(10);
        Battle battle = battleAgainst(enemy);

        battle.heroAttack();

        assertEquals(BattleOutcome.VICTORY, battle.outcome());
        assertEquals(hero.maxHp(), hero.currentHp());
        assertTrue(published.contains(new GameEvent.CombatantDefeated(enemy)));
        assertTrue(published.contains(new GameEvent.BattleEnded(BattleOutcome.VICTORY)));
    }

    @Test
    void heroDyingLosesTheBattle() {
        hero.takeDamage(hero.maxHp() - 5);
        Battle battle = battleAgainst(enemyWithHp(100));

        battle.heroAttack();

        assertEquals(BattleOutcome.DEFEAT, battle.outcome());
        assertTrue(published.contains(new GameEvent.CombatantDefeated(hero)));
        assertTrue(published.contains(new GameEvent.BattleEnded(BattleOutcome.DEFEAT)));
    }

    @Test
    void abilityGoesOnCooldownAndBecomesReadyAgain() {
        Battle battle = battleAgainst(enemyWithHp(1000));
        int cooldown = hero.ability().cooldownTurns();

        battle.heroUseAbility();
        assertEquals(cooldown, battle.abilityCooldownLeft());
        assertThrows(IllegalStateException.class, battle::heroUseAbility);

        for (int turn = 0; turn < cooldown; turn++) {
            battle.heroAttack();
        }
        assertEquals(0, battle.abilityCooldownLeft());
        battle.heroUseAbility();
        assertTrue(published.contains(new GameEvent.AbilityUsed(hero, hero.ability().name())));
    }

    @Test
    void usingAnItemConsumesItAndHeals() {
        hero.inventory().add(POTION);
        hero.takeDamage(50);
        Battle battle = battleAgainst(enemyWithHp(100));

        battle.heroUseItem(POTION);

        assertTrue(hero.inventory().items().isEmpty());
        assertEquals(hero.maxHp() - 50 + 30 - DAMAGE_PER_HIT, hero.currentHp());
        assertTrue(published.contains(new GameEvent.ItemUsed(hero, POTION)));
    }

    @Test
    void usingAMissingItemDoesNotPlayATurn() {
        Enemy enemy = enemyWithHp(100);
        Battle battle = battleAgainst(enemy);

        assertThrows(IllegalArgumentException.class, () -> battle.heroUseItem(POTION));
        assertEquals(hero.maxHp(), hero.currentHp());
        assertTrue(published.isEmpty());
    }

    @Test
    void noActionIsAllowedAfterTheEnd() {
        Battle battle = battleAgainst(enemyWithHp(10));
        battle.heroAttack();

        assertThrows(IllegalStateException.class, battle::heroAttack);
    }

    private Battle battleAgainst(Enemy enemy) {
        return new Battle(hero, enemy, new RecordingDamageResolver(DAMAGE_PER_HIT), events);
    }

    private static Enemy enemyWithHp(int hp) {
        return new Enemy("Goblin", new Stats(hp, 5, 0), 20, new AggressiveBehavior(), List.of());
    }
}
