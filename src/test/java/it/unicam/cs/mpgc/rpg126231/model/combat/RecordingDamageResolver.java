package it.unicam.cs.mpgc.rpg126231.model.combat;

import java.util.ArrayList;
import java.util.List;

/**
 * Risolutore finto per i test: registra i colpi ricevuti e toglie al bersaglio
 * un danno fisso, così i risultati sono prevedibili.
 */
public class RecordingDamageResolver implements DamageResolver {

    /**
     * Colpo registrato.
     *
     * @param attacker chi ha colpito
     * @param defender chi è stato colpito
     * @param modifier modificatori usati
     */
    public record Hit(Combatant attacker, Combatant defender, HitModifier modifier) {
    }

    private final int damagePerHit;
    private final List<Hit> hits = new ArrayList<>();

    /**
     * Crea il risolutore.
     *
     * @param damagePerHit danno fisso applicato a ogni colpo
     */
    public RecordingDamageResolver(int damagePerHit) {
        this.damagePerHit = damagePerHit;
    }

    @Override
    public int hit(Combatant attacker, Combatant defender, HitModifier modifier) {
        hits.add(new Hit(attacker, defender, modifier));
        return defender.takeDamage(damagePerHit);
    }

    /**
     * Restituisce i colpi registrati, in ordine.
     *
     * @return colpi registrati
     */
    public List<Hit> hits() {
        return hits;
    }
}
