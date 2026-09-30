package it.unicam.cs.mpgc.rpg126231.model.hero;

import it.unicam.cs.mpgc.rpg126231.model.ability.Ability;
import it.unicam.cs.mpgc.rpg126231.model.ability.Fireball;
import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;

/**
 * Mago: attacco elevato ma pochi punti vita e poca difesa.
 */
public class Mage implements HeroClass {

    private static final Stats BASE_STATS = new Stats(80, 18, 4);
    private static final Stats GROWTH = new Stats(10, 4, 1);

    private final Ability ability = new Fireball();

    @Override
    public String id() {
        return "mage";
    }

    @Override
    public String displayName() {
        return "Mago";
    }

    @Override
    public Stats baseStats() {
        return BASE_STATS;
    }

    @Override
    public Stats growthPerLevel() {
        return GROWTH;
    }

    @Override
    public Ability ability() {
        return ability;
    }
}
