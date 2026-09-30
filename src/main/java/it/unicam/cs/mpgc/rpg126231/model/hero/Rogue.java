package it.unicam.cs.mpgc.rpg126231.model.hero;

import it.unicam.cs.mpgc.rpg126231.model.ability.Ability;
import it.unicam.cs.mpgc.rpg126231.model.ability.DoubleStrike;
import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;

/**
 * Ladro: statistiche equilibrate e un'abilità che colpisce due volte.
 */
public class Rogue implements HeroClass {

    private static final Stats BASE_STATS = new Stats(100, 16, 6);
    private static final Stats GROWTH = new Stats(12, 3, 2);

    private final Ability ability = new DoubleStrike();

    @Override
    public String id() {
        return "rogue";
    }

    @Override
    public String displayName() {
        return "Ladro";
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
