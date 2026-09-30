package it.unicam.cs.mpgc.rpg126231.model.enemy;

import it.unicam.cs.mpgc.rpg126231.model.combat.Combatant;
import it.unicam.cs.mpgc.rpg126231.model.combat.DamageResolver;

/**
 * Intelligenza artificiale di un nemico (pattern Strategy): decide ed esegue
 * la mossa del nemico nel suo turno. Le implementazioni non hanno stato,
 * quindi la stessa istanza può essere condivisa da più nemici.
 */
public interface EnemyBehavior {

    /**
     * Esegue il turno del nemico.
     *
     * @param self     nemico che agisce
     * @param target   avversario del nemico
     * @param resolver risolutore dei colpi
     */
    void act(Enemy self, Combatant target, DamageResolver resolver);
}
