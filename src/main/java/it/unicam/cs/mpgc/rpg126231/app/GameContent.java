package it.unicam.cs.mpgc.rpg126231.app;

import it.unicam.cs.mpgc.rpg126231.model.Registry;
import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;
import it.unicam.cs.mpgc.rpg126231.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg126231.model.dungeon.Floor;
import it.unicam.cs.mpgc.rpg126231.model.enemy.AggressiveBehavior;
import it.unicam.cs.mpgc.rpg126231.model.enemy.BerserkerBehavior;
import it.unicam.cs.mpgc.rpg126231.model.enemy.EnemyBehavior;
import it.unicam.cs.mpgc.rpg126231.model.enemy.EnemyTemplate;
import it.unicam.cs.mpgc.rpg126231.model.hero.HeroClass;
import it.unicam.cs.mpgc.rpg126231.model.hero.Mage;
import it.unicam.cs.mpgc.rpg126231.model.hero.Rogue;
import it.unicam.cs.mpgc.rpg126231.model.hero.Warrior;
import it.unicam.cs.mpgc.rpg126231.model.item.Item;
import it.unicam.cs.mpgc.rpg126231.model.item.Potion;
import it.unicam.cs.mpgc.rpg126231.model.item.Weapon;

import java.util.List;

/**
 * Contenuto della prima release: classi di eroe, oggetti, nemici e struttura del dungeon.
 * Per aggiungere contenuto si registra qui un nuovo elemento, senza modificare
 * le classi del dominio o della logica di gioco.
 */
public final class GameContent {

    private static final Potion SMALL_POTION = new Potion("small_potion", "Pozione piccola", 30);
    private static final Potion LARGE_POTION = new Potion("large_potion", "Pozione grande", 60);
    private static final Weapon DAGGER = new Weapon("dagger", "Pugnale", 4);
    private static final Weapon SWORD = new Weapon("sword", "Spada", 5);
    private static final Weapon STAFF = new Weapon("staff", "Bastone runico", 6);

    private GameContent() {
    }

    /**
     * Crea il registro delle classi di eroe giocabili.
     *
     * @return registro con Guerriero, Mago e Ladro
     */
    public static Registry<HeroClass> heroClasses() {
        Registry<HeroClass> registry = new Registry<>();
        registry.register(new Warrior());
        registry.register(new Mage());
        registry.register(new Rogue());
        return registry;
    }

    /**
     * Crea il registro di tutti gli oggetti del gioco.
     *
     * @return registro con pozioni e armi
     */
    public static Registry<Item> items() {
        Registry<Item> registry = new Registry<>();
        List.of(SMALL_POTION, LARGE_POTION, DAGGER, SWORD, STAFF).forEach(registry::register);
        return registry;
    }

    /**
     * Crea il dungeon: cinque piani da due combattimenti, con il Drago come ultimo nemico.
     *
     * @return dungeon della prima release
     */
    public static Dungeon dungeon() {
        EnemyBehavior aggressive = new AggressiveBehavior();
        EnemyBehavior berserker = new BerserkerBehavior();
        EnemyTemplate goblin = new EnemyTemplate(
                "Goblin", new Stats(40, 10, 2), 30, aggressive, List.of(SMALL_POTION, DAGGER));
        EnemyTemplate wolf = new EnemyTemplate(
                "Lupo", new Stats(55, 13, 3), 45, aggressive, List.of(SMALL_POTION));
        EnemyTemplate skeleton = new EnemyTemplate(
                "Scheletro", new Stats(70, 15, 6), 60, aggressive, List.of(SMALL_POTION, SWORD));
        EnemyTemplate orc = new EnemyTemplate(
                "Orco", new Stats(95, 17, 6), 90, berserker, List.of(LARGE_POTION, STAFF));
        EnemyTemplate dragon = new EnemyTemplate(
                "Drago", new Stats(170, 22, 7), 200, berserker, List.of());
        return new Dungeon(List.of(
                new Floor(List.of(goblin, wolf)),
                new Floor(List.of(wolf, skeleton)),
                new Floor(List.of(skeleton, orc)),
                new Floor(List.of(orc, skeleton)),
                new Floor(List.of(orc, dragon))));
    }
}
