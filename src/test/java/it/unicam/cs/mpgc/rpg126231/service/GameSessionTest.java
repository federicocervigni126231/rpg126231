package it.unicam.cs.mpgc.rpg126231.service;

import it.unicam.cs.mpgc.rpg126231.model.combat.RecordingDamageResolver;
import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;
import it.unicam.cs.mpgc.rpg126231.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg126231.model.dungeon.DungeonProgress;
import it.unicam.cs.mpgc.rpg126231.model.dungeon.Floor;
import it.unicam.cs.mpgc.rpg126231.model.enemy.AggressiveBehavior;
import it.unicam.cs.mpgc.rpg126231.model.enemy.EnemyTemplate;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameSessionTest {

    /** Ogni colpo toglie 1000 punti vita: chi viene colpito muore subito. */
    private static final int LETHAL_DAMAGE = 1000;
    private static final int EXPERIENCE_PER_ENEMY = 60;
    private static final Potion POTION = new Potion("small_potion", "Pozione piccola", 30);
    private static final EnemyTemplate GOBLIN = new EnemyTemplate(
            "Goblin", new Stats(10, 1, 0), EXPERIENCE_PER_ENEMY, new AggressiveBehavior(), List.of(POTION));
    private static final EnemyTemplate GIANT = new EnemyTemplate(
            "Gigante", new Stats(100_000, 1, 0), EXPERIENCE_PER_ENEMY, new AggressiveBehavior(), List.of());

    private final EventBus events = new EventBus();
    private final List<GameEvent> published = new ArrayList<>();
    private Hero hero;

    @BeforeEach
    void setUp() {
        events.subscribe(published::add);
        hero = new Hero("Aldo", new Warrior());
    }

    @Test
    void startNextBattleSpawnsTheEnemyOfTheCurrentPosition() {
        GameSession session = sessionIn(twoGoblinFloors());

        session.startNextBattle();

        assertTrue(session.isInBattle());
        assertEquals("Goblin", session.currentEnemy().orElseThrow().name());
        assertInstanceOf(GameEvent.BattleStarted.class, published.get(0));
    }

    @Test
    void victoryGivesExperienceAndLootAndAdvances() {
        GameSession session = sessionIn(twoGoblinFloors());

        session.startNextBattle();
        session.attack();

        assertFalse(session.isInBattle());
        assertEquals(EXPERIENCE_PER_ENEMY, hero.experience());
        assertEquals(List.of(POTION), hero.inventory().items());
        assertEquals(new DungeonProgress(1, 0), session.progress());
        assertTrue(published.contains(new GameEvent.ExperienceGained(EXPERIENCE_PER_ENEMY)));
        assertTrue(published.contains(new GameEvent.ItemLooted(POTION)));
    }

    @Test
    void clearingTheLastEncounterWinsTheGame() {
        GameSession session = sessionIn(twoGoblinFloors());

        winBattle(session);
        winBattle(session);

        assertTrue(session.isOver());
        assertEquals(2, hero.level());
        assertTrue(published.contains(new GameEvent.LeveledUp(2)));
        assertTrue(published.contains(new GameEvent.GameWon()));
        assertThrows(IllegalStateException.class, session::startNextBattle);
    }

    @Test
    void heroDefeatLosesTheGame() {
        GameSession session = sessionIn(giantFloor());

        session.startNextBattle();
        session.attack();

        assertTrue(session.isOver());
        assertFalse(session.isInBattle());
        assertTrue(published.contains(new GameEvent.GameLost()));
    }

    @Test
    void abilityCooldownIsReportedDuringBattle() {
        GameSession session = sessionIn(giantFloor(), 1);

        assertEquals(0, session.abilityCooldownLeft());
        session.startNextBattle();
        session.useAbility();

        assertEquals(hero.ability().cooldownTurns(), session.abilityCooldownLeft());
    }

    @Test
    void itemCanBeUsedDuringBattle() {
        GameSession session = sessionIn(giantFloor(), 1);
        hero.inventory().add(POTION);

        session.startNextBattle();
        session.useItem(POTION);

        assertTrue(session.isInBattle());
        assertTrue(published.contains(new GameEvent.ItemUsed(hero, POTION)));
    }

    @Test
    void actionsRequireABattle() {
        GameSession session = sessionIn(twoGoblinFloors());

        assertThrows(IllegalStateException.class, session::attack);
        assertThrows(IllegalStateException.class, session::useAbility);
        assertThrows(IllegalStateException.class, () -> session.useItem(POTION));
    }

    @Test
    void onlyOneBattleAtATime() {
        GameSession session = sessionIn(twoGoblinFloors());
        session.startNextBattle();

        assertThrows(IllegalStateException.class, session::startNextBattle);
    }

    @Test
    void positionOutsideTheDungeonIsRejected() {
        Dungeon dungeon = twoGoblinFloors();
        assertThrows(IllegalArgumentException.class, () -> new GameSession(hero, dungeon,
                new DungeonProgress(5, 0), new RecordingDamageResolver(LETHAL_DAMAGE),
                new LootGenerator(() -> 0L, 100), events));
    }

    private GameSession sessionIn(Dungeon dungeon) {
        return sessionIn(dungeon, LETHAL_DAMAGE);
    }

    private GameSession sessionIn(Dungeon dungeon, int damagePerHit) {
        return new GameSession(hero, dungeon, DungeonProgress.START,
                new RecordingDamageResolver(damagePerHit), new LootGenerator(() -> 0L, 100), events);
    }

    private static Dungeon giantFloor() {
        return new Dungeon(List.of(new Floor(List.of(GIANT))));
    }

    private static Dungeon twoGoblinFloors() {
        return new Dungeon(List.of(new Floor(List.of(GOBLIN)), new Floor(List.of(GOBLIN))));
    }

    private static void winBattle(GameSession session) {
        session.startNextBattle();
        session.attack();
    }
}
