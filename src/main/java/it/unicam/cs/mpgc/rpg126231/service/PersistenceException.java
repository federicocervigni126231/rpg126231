package it.unicam.cs.mpgc.rpg126231.service;

/**
 * Errore nel salvare o caricare una partita. Nasconde le eccezioni specifiche
 * dell'implementazione (file, JSON, database) a chi usa il {@link GameRepository}.
 */
public class PersistenceException extends RuntimeException {

    /**
     * Crea l'eccezione.
     *
     * @param message descrizione dell'errore
     */
    public PersistenceException(String message) {
        super(message);
    }

    /**
     * Crea l'eccezione.
     *
     * @param message descrizione dell'errore
     * @param cause   eccezione originale
     */
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
