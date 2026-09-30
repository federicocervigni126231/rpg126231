package it.unicam.cs.mpgc.rpg126231.model.dungeon;

import it.unicam.cs.mpgc.rpg126231.model.enemy.EnemyTemplate;

import java.util.List;

/**
 * Piano del dungeon: la sequenza di nemici da affrontare, uno per combattimento.
 *
 * @param encounters nemici del piano, in ordine, almeno uno
 */
public record Floor(List<EnemyTemplate> encounters) {

    /**
     * Crea il piano copiando la lista e verificando che non sia vuota.
     *
     * @throws IllegalArgumentException se il piano non ha combattimenti
     */
    public Floor {
        encounters = List.copyOf(encounters);
        if (encounters.isEmpty()) {
            throw new IllegalArgumentException("Un piano deve avere almeno un combattimento");
        }
    }
}
