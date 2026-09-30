package it.unicam.cs.mpgc.rpg126231.model.combat;

/**
 * Statistiche di combattimento immutabili.
 *
 * @param maxHp   punti vita massimi
 * @param attack  potenza d'attacco
 * @param defense difesa
 */
public record Stats(int maxHp, int attack, int defense) {

    /**
     * Crea le statistiche verificando che nessun valore sia negativo.
     *
     * @throws IllegalArgumentException se un valore è negativo
     */
    public Stats {
        if (maxHp < 0 || attack < 0 || defense < 0) {
            throw new IllegalArgumentException("Le statistiche non possono essere negative");
        }
    }

    /**
     * Somma queste statistiche ad altre.
     *
     * @param other statistiche da sommare
     * @return nuove statistiche con i valori sommati
     */
    public Stats plus(Stats other) {
        return new Stats(maxHp + other.maxHp, attack + other.attack, defense + other.defense);
    }

    /**
     * Moltiplica ogni valore per un fattore.
     *
     * @param factor fattore non negativo
     * @return nuove statistiche con i valori moltiplicati
     */
    public Stats times(int factor) {
        return new Stats(maxHp * factor, attack * factor, defense * factor);
    }
}
