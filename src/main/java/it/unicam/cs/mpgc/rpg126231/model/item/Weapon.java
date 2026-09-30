package it.unicam.cs.mpgc.rpg126231.model.item;

/**
 * Arma equipaggiabile che aumenta la potenza d'attacco dell'eroe.
 *
 * @param id          identificativo dell'oggetto
 * @param name        nome mostrato al giocatore
 * @param attackBonus bonus d'attacco, non negativo
 */
public record Weapon(String id, String name, int attackBonus) implements Item {

    /**
     * Crea l'arma verificando che il bonus non sia negativo.
     *
     * @throws IllegalArgumentException se il bonus è negativo
     */
    public Weapon {
        if (attackBonus < 0) {
            throw new IllegalArgumentException("Il bonus d'attacco non può essere negativo");
        }
    }
}
