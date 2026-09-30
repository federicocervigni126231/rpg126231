package it.unicam.cs.mpgc.rpg126231.model.item;

import it.unicam.cs.mpgc.rpg126231.model.combat.Combatant;

/**
 * Pozione che ripristina punti vita.
 *
 * @param id         identificativo dell'oggetto
 * @param name       nome mostrato al giocatore
 * @param healAmount punti vita ripristinati, positivi
 */
public record Potion(String id, String name, int healAmount) implements Consumable {

    /**
     * Crea la pozione verificando che la cura sia positiva.
     *
     * @throws IllegalArgumentException se la cura non è positiva
     */
    public Potion {
        if (healAmount <= 0) {
            throw new IllegalArgumentException("La cura deve essere positiva");
        }
    }

    @Override
    public void consume(Combatant target) {
        target.heal(healAmount);
    }
}
