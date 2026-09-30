package it.unicam.cs.mpgc.rpg126231.service;

import it.unicam.cs.mpgc.rpg126231.model.Registry;
import it.unicam.cs.mpgc.rpg126231.model.combat.RecordingDamageResolver;
import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;
import it.unicam.cs.mpgc.rpg126231.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg126231.model.dungeon.DungeonProgress;
import it.unicam.cs.mpgc.rpg126231.model.dungeon.Floor;
import it.unicam.cs.mpgc.rpg126231.model.enemy.AggressiveBehavior;
import it.unicam.cs.mpgc.rpg126231.model.enemy.EnemyTemplate;
import it.unicam.cs.mpgc.rpg126231.model.hero.Hero;
import it.unicam.cs.mpgc.rpg126231.model.hero.HeroClass;
import it.unicam.cs.mpgc.rpg126231.model.hero.Mage;
import it.unicam.cs.mpgc.rpg126231.model.hero.Warrior;
import it.unicam.cs.mpgc.rpg126231.service.event.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameServiceTest {

    /** Repository in memoria: sostituisce il file nei test. */
    private static final class InMemoryGameRepository implements GameRepository {

        private SavedGame saved;

        @Override
        public void save(SavedGame game) {
            saved = game;
        }

        @Override
        public Optional<SavedGame> load() {
            return Optional.ofNullable(saved);
        }

        @Override
        public boolean exists() {
            return saved != null;
        }
    }

    private static final EnemyTemplate GOBLIN = new EnemyTemplate(
            "Goblin", new Stats(10, 1, 0), 30, new AggressiveBehavior(), List.of());

    private InMemoryGameRepository repository;
    private GameService service;

    @BeforeEach
    void setUp() {
        Registry<HeroClass> heroClasses = new Registry<>();
        heroClasses.register(new Warrior());
        heroClasses.register(new Mage());
        repository = new InMemoryGameRepository();
        service = new GameService(heroClasses,
                new Dungeon(List.of(new Floor(List.of(GOBLIN, GOBLIN)))),
                repository,
                new RecordingDamageResolver(1000),
                new LootGenerator(() -> 0L, 0),
                new EventBus());
    }

    @Test
    void availableHeroClassesComeFromTheRegistry() {
        assertEquals(List.of("warrior", "mage"),
                service.availableHeroClasses().stream().map(HeroClass::id).toList());
    }

    @Test
    void newGameStartsAtTheBeginningWithTheChosenClass() {
        GameSession session = service.newGame("Merlino", "mage");

        assertEquals("Merlino", session.hero().name());
        assertEquals("mage", session.hero().heroClass().id());
        assertEquals(DungeonProgress.START, session.progress());
    }

    @Test
    void unknownHeroClassIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> service.newGame("Aldo", "paladin"));
    }

    @Test
    void savedGameCanBeResumed() {
        GameSession session = service.newGame("Aldo", "warrior");
        session.startNextBattle();
        session.attack();

        service.saveGame(session);
        GameSession loaded = service.loadGame().orElseThrow();

        assertTrue(service.hasSavedGame());
        assertSame(session.hero(), loaded.hero());
        assertEquals(new DungeonProgress(0, 1), loaded.progress());
        assertFalse(loaded.isInBattle());
    }

    @Test
    void loadWithoutSaveIsEmpty() {
        assertFalse(service.hasSavedGame());
        assertTrue(service.loadGame().isEmpty());
    }

    @Test
    void savingDuringBattleIsRejected() {
        GameSession session = service.newGame("Aldo", "warrior");
        session.startNextBattle();

        assertThrows(IllegalStateException.class, () -> service.saveGame(session));
    }

    @Test
    void savingAFinishedGameIsRejected() {
        GameSession session = service.newGame("Aldo", "warrior");
        for (int i = 0; i < 2; i++) {
            session.startNextBattle();
            session.attack();
        }

        assertTrue(session.isOver());
        assertThrows(IllegalStateException.class, () -> service.saveGame(session));
    }

    @Test
    void saveOutsideTheDungeonIsRejectedOnLoad() {
        repository.save(new SavedGame(new Hero("Aldo", new Warrior()), new DungeonProgress(3, 0)));

        assertThrows(PersistenceException.class, service::loadGame);
    }
}
