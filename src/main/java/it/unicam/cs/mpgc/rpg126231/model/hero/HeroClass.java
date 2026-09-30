package it.unicam.cs.mpgc.rpg126231.model.hero;

import it.unicam.cs.mpgc.rpg126231.model.Identifiable;
import it.unicam.cs.mpgc.rpg126231.model.ability.Ability;
import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;

/**
 * Classe di un eroe: definisce statistiche iniziali, crescita per livello e abilità speciale.
 * Una nuova classe di eroe si aggiunge implementando questa interfaccia.
 */
public interface HeroClass extends Identifiable {

    /**
     * Restituisce il nome della classe mostrato al giocatore.
     *
     * @return nome della classe
     */
    String displayName();

    /**
     * Restituisce le statistiche al livello 1.
     *
     * @return statistiche iniziali
     */
    Stats baseStats();

    /**
     * Restituisce l'aumento di statistiche guadagnato a ogni livello.
     *
     * @return crescita per livello
     */
    Stats growthPerLevel();

    /**
     * Restituisce l'abilità speciale della classe.
     *
     * @return abilità speciale
     */
    Ability ability();
}
