package it.unicam.cs.mpgc.rpg126231.persistence;

import it.unicam.cs.mpgc.rpg126231.model.Identifiable;
import it.unicam.cs.mpgc.rpg126231.model.Registry;
import it.unicam.cs.mpgc.rpg126231.model.dungeon.DungeonProgress;
import it.unicam.cs.mpgc.rpg126231.model.hero.Hero;
import it.unicam.cs.mpgc.rpg126231.model.hero.HeroClass;
import it.unicam.cs.mpgc.rpg126231.model.item.Inventory;
import it.unicam.cs.mpgc.rpg126231.model.item.Item;
import it.unicam.cs.mpgc.rpg126231.model.item.Weapon;
import it.unicam.cs.mpgc.rpg126231.service.SavedGame;

import java.util.Objects;

/**
 * Converte una partita del dominio nei dati da salvare, e viceversa.
 * Per ricostruire classe dell'eroe e oggetti usa i registri, a partire dagli identificativi.
 */
class SaveDataMapper {

    private final Registry<HeroClass> heroClasses;
    private final Registry<Item> items;

    /**
     * Crea il convertitore.
     *
     * @param heroClasses registro delle classi di eroe
     * @param items       registro degli oggetti
     */
    SaveDataMapper(Registry<HeroClass> heroClasses, Registry<Item> items) {
        this.heroClasses = Objects.requireNonNull(heroClasses, "heroClasses");
        this.items = Objects.requireNonNull(items, "items");
    }

    /**
     * Converte la partita nei dati da salvare.
     *
     * @param game partita
     * @return dati da salvare
     */
    SaveData toData(SavedGame game) {
        Hero hero = game.hero();
        SaveData.HeroData heroData = new SaveData.HeroData(
                hero.name(),
                hero.heroClass().id(),
                hero.level(),
                hero.experience(),
                hero.currentHp(),
                hero.inventory().items().stream().map(Identifiable::id).toList(),
                hero.equippedWeapon().map(Weapon::id).orElse(null));
        return new SaveData(heroData, game.progress().floor(), game.progress().encounter());
    }

    /**
     * Ricostruisce la partita dai dati salvati.
     *
     * @param data dati salvati
     * @return partita ricostruita
     * @throws IllegalArgumentException se i dati non sono validi, per esempio un
     *                                  identificativo sconosciuto o un'arma che non è un'arma
     * @throws NullPointerException     se mancano dati obbligatori
     */
    SavedGame fromData(SaveData data) {
        SaveData.HeroData heroData = Objects.requireNonNull(data.hero(), "hero");
        Inventory inventory = new Inventory();
        heroData.inventoryItemIds().forEach(id -> inventory.add(items.get(id)));
        Hero hero = new Hero(
                heroData.name(),
                heroClasses.get(heroData.heroClassId()),
                heroData.level(),
                heroData.experience(),
                heroData.currentHp(),
                inventory,
                heroData.equippedWeaponId() == null ? null : weapon(heroData.equippedWeaponId()));
        return new SavedGame(hero, new DungeonProgress(data.floor(), data.encounter()));
    }

    private Weapon weapon(String id) {
        if (items.get(id) instanceof Weapon weapon) {
            return weapon;
        }
        throw new IllegalArgumentException("L'oggetto equipaggiato non è un'arma: " + id);
    }
}
