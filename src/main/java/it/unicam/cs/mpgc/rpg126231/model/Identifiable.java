package it.unicam.cs.mpgc.rpg126231.model;

/**
 * Elemento del gioco riconoscibile tramite un identificativo testuale stabile,
 * usato per registrarlo in un {@link Registry} e per salvarlo.
 */
public interface Identifiable {

    /**
     * Restituisce l'identificativo univoco dell'elemento.
     *
     * @return identificativo, mai vuoto
     */
    String id();
}
