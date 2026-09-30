package it.unicam.cs.mpgc.rpg126231.service;

import java.util.Optional;

/**
 * Archivio della partita salvata (pattern Repository). La logica di gioco dipende
 * solo da questa interfaccia: il formato e il supporto (file JSON, database)
 * sono scelti dall'implementazione.
 */
public interface GameRepository {

    /**
     * Salva la partita, sostituendo quella salvata in precedenza.
     *
     * @param game partita da salvare
     * @throws PersistenceException se il salvataggio non riesce
     */
    void save(SavedGame game);

    /**
     * Carica la partita salvata.
     *
     * @return partita salvata, vuota se non ce n'è una
     * @throws PersistenceException se il salvataggio esiste ma non si può leggere
     */
    Optional<SavedGame> load();

    /**
     * Indica se esiste una partita salvata.
     *
     * @return {@code true} se esiste un salvataggio
     */
    boolean exists();
}
