package it.unicam.cs.mpgc.rpg126231.service;

import it.unicam.cs.mpgc.rpg126231.model.combat.Combatant;
import it.unicam.cs.mpgc.rpg126231.model.combat.DamageResolver;
import it.unicam.cs.mpgc.rpg126231.model.combat.HitModifier;
import it.unicam.cs.mpgc.rpg126231.service.event.EventBus;
import it.unicam.cs.mpgc.rpg126231.service.event.GameEvent;

import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Formula del danno del gioco:
 * {@code max(1, attacco × moltiplicatore − difesa) + casuale(0..2)}.
 * Applica il danno al bersaglio e pubblica l'evento corrispondente.
 */
public class DamageCalculator implements DamageResolver {

    private static final int MIN_DAMAGE = 1;
    private static final int MAX_RANDOM_BONUS = 2;

    private final RandomGenerator random;
    private final EventBus events;

    /**
     * Crea il calcolatore.
     *
     * @param random generatore casuale, sostituibile nei test
     * @param events canale su cui pubblicare i colpi
     */
    public DamageCalculator(RandomGenerator random, EventBus events) {
        this.random = Objects.requireNonNull(random, "random");
        this.events = Objects.requireNonNull(events, "events");
    }

    @Override
    public int hit(Combatant attacker, Combatant defender, HitModifier modifier) {
        int power = (int) Math.round(attacker.attackPower() * modifier.multiplier());
        int defense = modifier.ignoresDefense() ? 0 : defender.defense();
        int damage = Math.max(MIN_DAMAGE, power - defense) + random.nextInt(MAX_RANDOM_BONUS + 1);
        int dealt = defender.takeDamage(damage);
        events.publish(new GameEvent.DamageDealt(attacker, defender, dealt));
        return dealt;
    }
}
