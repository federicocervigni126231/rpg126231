package it.unicam.cs.mpgc.rpg126231.model.enemy;

import it.unicam.cs.mpgc.rpg126231.model.combat.Combatant;
import it.unicam.cs.mpgc.rpg126231.model.combat.DamageResolver;
import it.unicam.cs.mpgc.rpg126231.model.combat.HitModifier;

/**
 * Comportamento berserker: quando i punti vita del nemico scendono al 30% o meno,
 * i suoi colpi diventano più forti.
 */
public class BerserkerBehavior implements EnemyBehavior {

    private static final int RAGE_THRESHOLD_PERCENT = 30;
    private static final HitModifier RAGE_HIT = new HitModifier(1.5, false);

    @Override
    public void act(Enemy self, Combatant target, DamageResolver resolver) {
        resolver.hit(self, target, isEnraged(self) ? RAGE_HIT : HitModifier.NORMAL);
    }

    private static boolean isEnraged(Enemy self) {
        return self.currentHp() * 100 <= self.maxHp() * RAGE_THRESHOLD_PERCENT;
    }
}
