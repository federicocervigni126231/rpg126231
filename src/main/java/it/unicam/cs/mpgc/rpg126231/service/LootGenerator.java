package it.unicam.cs.mpgc.rpg126231.service;

import it.unicam.cs.mpgc.rpg126231.model.enemy.Enemy;
import it.unicam.cs.mpgc.rpg126231.model.item.Item;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.random.RandomGenerator;

/**
 * Decide se un nemico sconfitto lascia un oggetto e quale, scegliendolo a caso
 * tra i suoi possibili oggetti.
 */
public class LootGenerator {

    private static final int PERCENT = 100;

    private final RandomGenerator random;
    private final int dropChancePercent;

    /**
     * Crea il generatore.
     *
     * @param random            generatore casuale, sostituibile nei test
     * @param dropChancePercent probabilità di ottenere un oggetto, tra 0 e 100
     */
    public LootGenerator(RandomGenerator random, int dropChancePercent) {
        if (dropChancePercent < 0 || dropChancePercent > PERCENT) {
            throw new IllegalArgumentException("La probabilità deve essere tra 0 e 100");
        }
        this.random = Objects.requireNonNull(random, "random");
        this.dropChancePercent = dropChancePercent;
    }

    /**
     * Estrae l'oggetto lasciato da un nemico sconfitto.
     *
     * @param enemy nemico sconfitto
     * @return oggetto ottenuto, vuoto se il nemico non lascia nulla
     */
    public Optional<Item> roll(Enemy enemy) {
        List<Item> drops = enemy.possibleDrops();
        if (drops.isEmpty() || random.nextInt(PERCENT) >= dropChancePercent) {
            return Optional.empty();
        }
        return Optional.of(drops.get(random.nextInt(drops.size())));
    }
}
