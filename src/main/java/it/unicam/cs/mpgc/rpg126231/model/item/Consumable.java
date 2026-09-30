package it.unicam.cs.mpgc.rpg126231.model.item;

import it.unicam.cs.mpgc.rpg126231.model.combat.Combatant;

/**
 * Oggetto che si consuma usandolo su un combattente.
 */
public interface Consumable extends Item {

    /**
     * Applica l'effetto dell'oggetto.
     *
     * @param target combattente su cui usare l'oggetto
     */
    void consume(Combatant target);
}
