package it.unicam.cs.mpgc.rpg126231.model.enemy;

import it.unicam.cs.mpgc.rpg126231.model.combat.Combatant;
import it.unicam.cs.mpgc.rpg126231.model.combat.DamageResolver;
import it.unicam.cs.mpgc.rpg126231.model.combat.HitModifier;

/**
 * Comportamento aggressivo: il nemico attacca sempre con un colpo normale.
 */
public class AggressiveBehavior implements EnemyBehavior {

    @Override
    public void act(Enemy self, Combatant target, DamageResolver resolver) {
        resolver.hit(self, target, HitModifier.NORMAL);
    }
}
