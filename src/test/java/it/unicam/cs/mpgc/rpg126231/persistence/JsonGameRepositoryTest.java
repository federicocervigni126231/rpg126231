package it.unicam.cs.mpgc.rpg126231.persistence;

import it.unicam.cs.mpgc.rpg126231.model.Registry;
import it.unicam.cs.mpgc.rpg126231.model.dungeon.DungeonProgress;
import it.unicam.cs.mpgc.rpg126231.model.hero.Hero;
import it.unicam.cs.mpgc.rpg126231.model.hero.HeroClass;
import it.unicam.cs.mpgc.rpg126231.model.hero.Mage;
import it.unicam.cs.mpgc.rpg126231.model.hero.Warrior;
import it.unicam.cs.mpgc.rpg126231.model.item.Inventory;
import it.unicam.cs.mpgc.rpg126231.model.item.Item;
import it.unicam.cs.mpgc.rpg126231.model.item.Potion;
import it.unicam.cs.mpgc.rpg126231.model.item.Weapon;
import it.unicam.cs.mpgc.rpg126231.service.PersistenceException;
import it.unicam.cs.mpgc.rpg126231.service.SavedGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonGameRepositoryTest {

    private static final Potion POTION = new Potion("small_potion", "Pozione piccola", 30);
    private static final Weapon SWORD = new Weapon("sword", "Spada", 5);
    private static final Weapon STAFF = new Weapon("staff", "Bastone runico", 6);

    @TempDir
    Path directory;

    private Path file;
    private JsonGameRepository repository;

    @BeforeEach
    void setUp() {
        Registry<HeroClass> heroClasses = new Registry<>();
        heroClasses.register(new Warrior());
        heroClasses.register(new Mage());
        Registry<Item> items = new Registry<>();
        List.of(POTION, SWORD, STAFF).forEach(items::register);
        file = directory.resolve("saves").resolve("save.json");
        repository = new JsonGameRepository(file, heroClasses, items);
    }

    @Test
    void nothingIsSavedAtFirst() {
        assertFalse(repository.exists());
        assertEquals(Optional.empty(), repository.load());
    }

    @Test
    void savedGameIsLoadedBackIdentical() {
        Inventory inventory = new Inventory();
        inventory.add(POTION);
        inventory.add(POTION);
        inventory.add(SWORD);
        Hero hero = new Hero("Merlino", new Mage(), 3, 40, 70, inventory, STAFF);

        repository.save(new SavedGame(hero, new DungeonProgress(2, 1)));
        SavedGame loaded = repository.load().orElseThrow();

        assertTrue(repository.exists());
        assertEquals(new DungeonProgress(2, 1), loaded.progress());
        Hero loadedHero = loaded.hero();
        assertEquals("Merlino", loadedHero.name());
        assertEquals("mage", loadedHero.heroClass().id());
        assertEquals(3, loadedHero.level());
        assertEquals(40, loadedHero.experience());
        assertEquals(70, loadedHero.currentHp());
        assertEquals(List.of(POTION, POTION, SWORD), loadedHero.inventory().items());
        assertEquals(Optional.of(STAFF), loadedHero.equippedWeapon());
    }

    @Test
    void heroWithoutWeaponIsSavedAndLoaded() {
        repository.save(new SavedGame(new Hero("Aldo", new Warrior()), DungeonProgress.START));

        assertTrue(repository.load().orElseThrow().hero().equippedWeapon().isEmpty());
    }

    @Test
    void newSaveReplacesThePreviousOne() {
        repository.save(new SavedGame(new Hero("Aldo", new Warrior()), DungeonProgress.START));
        repository.save(new SavedGame(new Hero("Bea", new Warrior()), new DungeonProgress(1, 0)));

        SavedGame loaded = repository.load().orElseThrow();
        assertEquals("Bea", loaded.hero().name());
        assertEquals(new DungeonProgress(1, 0), loaded.progress());
    }

    @Test
    void corruptedFileIsReported() throws IOException {
        Files.createDirectories(file.getParent());
        Files.writeString(file, "{ non è json");

        assertThrows(PersistenceException.class, repository::load);
    }

    @Test
    void emptyFileIsReported() throws IOException {
        Files.createDirectories(file.getParent());
        Files.writeString(file, "");

        assertThrows(PersistenceException.class, repository::load);
    }

    @Test
    void missingDataIsReported() throws IOException {
        Files.createDirectories(file.getParent());
        Files.writeString(file, "{ \"floor\": 0, \"encounter\": 0 }");

        assertThrows(PersistenceException.class, repository::load);
    }

    @Test
    void unknownIdentifierIsReported() throws IOException {
        repository.save(new SavedGame(new Hero("Aldo", new Warrior()), DungeonProgress.START));
        Files.writeString(file, Files.readString(file).replace("warrior", "paladin"));

        assertThrows(PersistenceException.class, repository::load);
    }

    @Test
    void equippedItemThatIsNotAWeaponIsReported() throws IOException {
        Inventory inventory = new Inventory();
        repository.save(new SavedGame(
                new Hero("Aldo", new Warrior(), 1, 0, 50, inventory, SWORD), DungeonProgress.START));
        Files.writeString(file, Files.readString(file).replace("\"sword\"", "\"small_potion\""));

        assertThrows(PersistenceException.class, repository::load);
    }
}
