package it.unicam.cs.mpgc.rpg126231.model.item;

import it.unicam.cs.mpgc.rpg126231.model.Identifiable;

/**
 * Oggetto che può stare nell'inventario dell'eroe.
 */
public interface Item extends Identifiable {

    /**
     * Restituisce il nome dell'oggetto mostrato al giocatore.
     *
     * @return nome dell'oggetto
     */
    String name();
}
