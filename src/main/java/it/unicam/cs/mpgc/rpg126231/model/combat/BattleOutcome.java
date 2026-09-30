package it.unicam.cs.mpgc.rpg126231.model.combat;

/**
 * Stato di un combattimento.
 */
public enum BattleOutcome {

    /** Il combattimento è ancora in corso. */
    ONGOING,

    /** Il nemico è stato sconfitto. */
    VICTORY,

    /** L'eroe è stato sconfitto. */
    DEFEAT
}
