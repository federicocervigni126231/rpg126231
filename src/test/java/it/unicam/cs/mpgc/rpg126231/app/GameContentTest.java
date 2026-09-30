package it.unicam.cs.mpgc.rpg126231.app;

import it.unicam.cs.mpgc.rpg126231.model.Registry;
import it.unicam.cs.mpgc.rpg126231.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg126231.model.dungeon.DungeonProgress;
import it.unicam.cs.mpgc.rpg126231.model.enemy.EnemyTemplate;
import it.unicam.cs.mpgc.rpg126231.model.item.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GameContentTest {

    @Test
    void threeHeroClassesArePlayable() {
        assertEquals(3, GameContent.heroClasses().all().size());
    }

    @Test
    void dungeonHasFiveFloorsAndEndsWithTheDragon() {
        List<EnemyTemplate> encounters = allEncounters(GameContent.dungeon());

        assertEquals(5, GameContent.dungeon().floorCount());
        assertEquals("Drago", encounters.get(encounters.size() - 1).name());
        assertEquals(5, encounters.stream().map(EnemyTemplate::name).distinct().count());
    }

    @Test
    void everyDroppableItemIsRegisteredSoItCanBeSaved() {
        Registry<Item> items = GameContent.items();
        for (EnemyTemplate encounter : allEncounters(GameContent.dungeon())) {
            for (Item drop : encounter.possibleDrops()) {
                assertEquals(drop, items.get(drop.id()), drop.id());
            }
        }
    }

    @Test
    void bootstrapWiresAWorkingGameService(@TempDir Path directory) {
        GameBootstrap bootstrap = new GameBootstrap(directory.resolve("save.json"));

        assertEquals(3, bootstrap.gameService().availableHeroClasses().size());
        assertFalse(bootstrap.gameService().hasSavedGame());
        assertDoesNotThrow(() -> bootstrap.gameService().newGame("Aldo", "rogue").startNextBattle());
    }

    private static List<EnemyTemplate> allEncounters(Dungeon dungeon) {
        List<EnemyTemplate> encounters = new ArrayList<>();
        Optional<DungeonProgress> position = Optional.of(DungeonProgress.START);
        while (position.isPresent()) {
            encounters.add(dungeon.encounterAt(position.get()));
            position = dungeon.next(position.get());
        }
        return encounters;
    }
}
