package it.unicam.cs.mpgc.rpg126231.model.hero;

import it.unicam.cs.mpgc.rpg126231.model.ability.Ability;
import it.unicam.cs.mpgc.rpg126231.model.ability.PowerStrike;
import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;

/**
 * Guerriero: molti punti vita e buona difesa.
 */
public class Warrior implements HeroClass {

    private static final Stats BASE_STATS = new Stats(120, 14, 8);
    private static final Stats GROWTH = new Stats(15, 3, 2);

    private final Ability ability = new PowerStrike();

    @Override
    public String id() {
        return "warrior";
    }

    @Override
    public String displayName() {
        return "Guerriero";
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
