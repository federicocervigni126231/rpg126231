package it.unicam.cs.mpgc.rpg126231.model.combat;

/**
 * Risolve un colpo tra due combattenti: calcola il danno e lo applica al bersaglio.
 * Abilità e comportamenti dei nemici dipendono da questa astrazione e non dalla
 * formula concreta del danno.
 */
public interface DamageResolver {

    /**
     * Esegue un colpo.
     *
     * @param attacker chi colpisce
     * @param defender chi subisce il colpo
     * @param modifier modificatori del colpo
     * @return punti vita effettivamente tolti al bersaglio
     */
    int hit(Combatant attacker, Combatant defender, HitModifier modifier);
}
