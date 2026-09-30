package it.unicam.cs.mpgc.rpg126231.model.combat;

import java.util.Objects;

/**
 * Partecipante a un combattimento: ha un nome, delle statistiche e dei punti vita
 * che possono diminuire con i danni e aumentare con le cure.
 */
public abstract class Combatant {

    private final String name;
    private int currentHp;

    /**
     * Inizializza nome e punti vita attuali.
     *
     * @param name      nome del combattente, non vuoto
     * @param currentHp punti vita attuali, non negativi
     */
    protected Combatant(String name, int currentHp) {
        Objects.requireNonNull(name, "name");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto");
        }
        if (currentHp < 0) {
            throw new IllegalArgumentException("I punti vita non possono essere negativi");
        }
        this.name = name;
        this.currentHp = currentHp;
    }

    /**
     * Restituisce le statistiche attuali del combattente.
     *
     * @return statistiche attuali
     */
    public abstract Stats stats();

    /**
     * Restituisce il nome del combattente.
     *
     * @return nome
     */
    public String name() {
        return name;
    }

    /**
     * Restituisce la potenza d'attacco usata per calcolare i danni.
     *
     * @return potenza d'attacco
     */
    public int attackPower() {
        return stats().attack();
    }

    /**
     * Restituisce la difesa usata per ridurre i danni subiti.
     *
     * @return difesa
     */
    public int defense() {
        return stats().defense();
    }

    /**
     * Restituisce i punti vita massimi.
     *
     * @return punti vita massimi
     */
    public int maxHp() {
        return stats().maxHp();
    }

    /**
     * Restituisce i punti vita attuali.
     *
     * @return punti vita attuali
     */
    public int currentHp() {
        return currentHp;
    }

    /**
     * Indica se il combattente è ancora in vita.
     *
     * @return {@code true} se ha almeno un punto vita
     */
    public boolean isAlive() {
        return currentHp > 0;
    }

    /**
     * Sottrae punti vita, senza scendere sotto zero.
     *
     * @param amount danno da subire, non negativo
     * @return punti vita effettivamente persi
     */
    public int takeDamage(int amount) {
        requireNonNegative(amount);
        int lost = Math.min(amount, currentHp);
        currentHp -= lost;
        return lost;
    }

    /**
     * Aggiunge punti vita, senza superare il massimo.
     *
     * @param amount cura da ricevere, non negativa
     * @return punti vita effettivamente recuperati
     */
    public int heal(int amount) {
        requireNonNegative(amount);
        int healed = Math.min(amount, maxHp() - currentHp);
        currentHp += healed;
        return healed;
    }

    /**
     * Riporta i punti vita al massimo.
     */
    protected void restoreFullHp() {
        currentHp = maxHp();
    }

    /**
     * Verifica che i punti vita attuali non superino il massimo.
     * Va chiamato dai costruttori delle sottoclassi, quando le statistiche sono disponibili.
     *
     * @throws IllegalArgumentException se i punti vita attuali superano il massimo
     */
    protected void requireValidHp() {
        if (currentHp > maxHp()) {
            throw new IllegalArgumentException("I punti vita superano il massimo");
        }
    }

    private static void requireNonNegative(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Il valore non può essere negativo");
        }
    }
}
