package it.unicam.cs.mpgc.rpg126231.model.ability;

import it.unicam.cs.mpgc.rpg126231.model.combat.Combatant;
import it.unicam.cs.mpgc.rpg126231.model.combat.DamageResolver;

/**
 * Abilità speciale di una classe di eroe (pattern Strategy): ogni implementazione
 * definisce un effetto diverso, e chi la usa non deve conoscerla.
 */
public interface Ability {

    /**
     * Restituisce il nome dell'abilità mostrato al giocatore.
     *
     * @return nome dell'abilità
     */
    String name();

    /**
     * Restituisce quanti turni bisogna attendere prima di riusarla.
     *
     * @return turni di ricarica, positivi
     */
    int cooldownTurns();

    /**
     * Esegue l'abilità.
     *
     * @param user     chi usa l'abilità
     * @param target   bersaglio dell'abilità
     * @param resolver risolutore dei colpi
     */
    void use(Combatant user, Combatant target, DamageResolver resolver);
}
