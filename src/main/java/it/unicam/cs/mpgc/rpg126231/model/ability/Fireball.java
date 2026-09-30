package it.unicam.cs.mpgc.rpg126231.model.ability;

import it.unicam.cs.mpgc.rpg126231.model.combat.Combatant;
import it.unicam.cs.mpgc.rpg126231.model.combat.DamageResolver;
import it.unicam.cs.mpgc.rpg126231.model.combat.HitModifier;

/**
 * Palla di Fuoco: un colpo potenziato che ignora la difesa del bersaglio.
 */
public class Fireball implements Ability {

    private static final HitModifier MAGIC_HIT = new HitModifier(1.5, true);
    private static final int COOLDOWN_TURNS = 3;

    @Override
    public String name() {
        return "Palla di Fuoco";
    }

    @Override
    public int cooldownTurns() {
        return COOLDOWN_TURNS;
    }

    @Override
    public void use(Combatant user, Combatant target, DamageResolver resolver) {
        resolver.hit(user, target, MAGIC_HIT);
    }
}
