package it.unicam.cs.mpgc.rpg126231.model.ability;

import it.unicam.cs.mpgc.rpg126231.model.combat.Combatant;
import it.unicam.cs.mpgc.rpg126231.model.combat.DamageResolver;
import it.unicam.cs.mpgc.rpg126231.model.combat.HitModifier;

/**
 * Doppio Colpo: due colpi normali consecutivi. Il secondo parte solo se
 * il bersaglio è ancora in vita.
 */
public class DoubleStrike implements Ability {

    private static final int COOLDOWN_TURNS = 2;

    @Override
    public String name() {
        return "Doppio Colpo";
    }

    @Override
    public int cooldownTurns() {
        return COOLDOWN_TURNS;
    }

    @Override
    public void use(Combatant user, Combatant target, DamageResolver resolver) {
        resolver.hit(user, target, HitModifier.NORMAL);
        if (target.isAlive()) {
            resolver.hit(user, target, HitModifier.NORMAL);
        }
    }
}
