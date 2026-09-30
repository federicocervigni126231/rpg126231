package it.unicam.cs.mpgc.rpg126231.model.enemy;

import it.unicam.cs.mpgc.rpg126231.model.combat.Combatant;
import it.unicam.cs.mpgc.rpg126231.model.combat.DamageResolver;
import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;
import it.unicam.cs.mpgc.rpg126231.model.item.Item;

import java.util.List;
import java.util.Objects;

/**
 * Nemico affrontato in combattimento. Delega la scelta della mossa al proprio
 * {@link EnemyBehavior} e, se sconfitto, dà esperienza e può lasciare un oggetto.
 */
public class Enemy extends Combatant {

    private final Stats stats;
    private final int experienceReward;
    private final EnemyBehavior behavior;
    private final List<Item> possibleDrops;

    /**
     * Crea un nemico con i punti vita al massimo.
     *
     * @param name             nome del nemico
     * @param stats            statistiche, con punti vita massimi positivi
     * @param experienceReward esperienza data all'eroe se sconfitto, non negativa
     * @param behavior         comportamento in combattimento
     * @param possibleDrops    oggetti che può lasciare se sconfitto
     */
    public Enemy(String name, Stats stats, int experienceReward, EnemyBehavior behavior,
                 List<Item> possibleDrops) {
        super(name, stats.maxHp());
        if (stats.maxHp() <= 0) {
            throw new IllegalArgumentException("Un nemico deve avere punti vita positivi");
        }
        if (experienceReward < 0) {
            throw new IllegalArgumentException("L'esperienza non può essere negativa");
        }
        this.stats = stats;
        this.experienceReward = experienceReward;
        this.behavior = Objects.requireNonNull(behavior, "behavior");
        this.possibleDrops = List.copyOf(possibleDrops);
    }

    @Override
    public Stats stats() {
        return stats;
    }

    /**
     * Esegue il turno del nemico secondo il suo comportamento.
     *
     * @param target   avversario del nemico
     * @param resolver risolutore dei colpi
     */
    public void takeTurn(Combatant target, DamageResolver resolver) {
        behavior.act(this, target, resolver);
    }

    /**
     * Restituisce l'esperienza data all'eroe quando il nemico viene sconfitto.
     *
     * @return esperienza
     */
    public int experienceReward() {
        return experienceReward;
    }

    /**
     * Restituisce gli oggetti che il nemico può lasciare quando viene sconfitto.
     *
     * @return lista non modificabile dei possibili oggetti
     */
    public List<Item> possibleDrops() {
        return possibleDrops;
    }
}
