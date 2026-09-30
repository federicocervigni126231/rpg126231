package it.unicam.cs.mpgc.rpg126231.model.dungeon;

/**
 * Posizione dell'eroe nel dungeon: il piano e il combattimento del piano,
 * entrambi contati da zero.
 *
 * @param floor     indice del piano
 * @param encounter indice del combattimento nel piano
 */
public record DungeonProgress(int floor, int encounter) {

    /** Posizione iniziale: primo combattimento del primo piano. */
    public static final DungeonProgress START = new DungeonProgress(0, 0);

    /**
     * Crea la posizione verificando che gli indici non siano negativi.
     *
     * @throws IllegalArgumentException se un indice è negativo
     */
    public DungeonProgress {
        if (floor < 0 || encounter < 0) {
            throw new IllegalArgumentException("Gli indici non possono essere negativi");
        }
    }
}
