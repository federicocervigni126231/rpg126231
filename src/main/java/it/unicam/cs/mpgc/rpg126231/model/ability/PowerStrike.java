package it.unicam.cs.mpgc.rpg126231.model.ability;

import it.unicam.cs.mpgc.rpg126231.model.combat.Combatant;
import it.unicam.cs.mpgc.rpg126231.model.combat.DamageResolver;
import it.unicam.cs.mpgc.rpg126231.model.combat.HitModifier;

/**
 * Colpo Potente: un singolo colpo con potenza d'attacco raddoppiata.
 */
public class PowerStrike implements Ability {

    private static final HitModifier DOUBLE_POWER = new HitModifier(2.0, false);
    private static final int COOLDOWN_TURNS = 3;

    @Override
    public String name() {
        return "Colpo Potente";
    }

    @Override
    public int cooldownTurns() {
        return COOLDOWN_TURNS;
    }

    @Override
    public void use(Combatant user, Combatant target, DamageResolver resolver) {
        resolver.hit(user, target, DOUBLE_POWER);
    }
}
