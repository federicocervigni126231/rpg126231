package it.unicam.cs.mpgc.rpg126231.service;

import it.unicam.cs.mpgc.rpg126231.model.dungeon.DungeonProgress;
import it.unicam.cs.mpgc.rpg126231.model.hero.Hero;

import java.util.Objects;

/**
 * Stato di una partita da salvare: l'eroe e la posizione nel dungeon.
 * Si salva solo tra un combattimento e l'altro, quindi non serve altro.
 *
 * @param hero     eroe del giocatore
 * @param progress prossimo combattimento da affrontare
 */
public record SavedGame(Hero hero, DungeonProgress progress) {

    /**
     * Crea lo stato verificando che i componenti siano presenti.
     */
    public SavedGame {
        Objects.requireNonNull(hero, "hero");
        Objects.requireNonNull(progress, "progress");
    }
}
