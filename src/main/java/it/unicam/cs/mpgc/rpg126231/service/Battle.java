package it.unicam.cs.mpgc.rpg126231.service;

import it.unicam.cs.mpgc.rpg126231.model.ability.Ability;
import it.unicam.cs.mpgc.rpg126231.model.combat.BattleOutcome;
import it.unicam.cs.mpgc.rpg126231.model.combat.Combatant;
import it.unicam.cs.mpgc.rpg126231.model.combat.DamageResolver;
import it.unicam.cs.mpgc.rpg126231.model.combat.HitModifier;
import it.unicam.cs.mpgc.rpg126231.model.enemy.Enemy;
import it.unicam.cs.mpgc.rpg126231.model.hero.Hero;
import it.unicam.cs.mpgc.rpg126231.model.item.Consumable;
import it.unicam.cs.mpgc.rpg126231.service.event.EventBus;
import it.unicam.cs.mpgc.rpg126231.service.event.GameEvent;

import java.util.Objects;

/**
 * Un combattimento a turni tra l'eroe e un nemico. Ogni azione dell'eroe è seguita
 * dal turno del nemico, se ancora in vita. Tiene traccia della ricarica dell'abilità
 * e dell'esito. È visibile solo nel package: dall'esterno si usa {@link GameSession}.
 */
class Battle {

    private final Hero hero;
    private final Enemy enemy;
    private final DamageResolver resolver;
    private final EventBus events;
    private int abilityCooldownLeft;
    private BattleOutcome outcome = BattleOutcome.ONGOING;

    /**
     * Crea il combattimento.
     *
     * @param hero     eroe
     * @param enemy    nemico
     * @param resolver risolutore dei colpi
     * @param events   canale degli eventi
     */
    Battle(Hero hero, Enemy enemy, DamageResolver resolver, EventBus events) {
        this.hero = Objects.requireNonNull(hero, "hero");
        this.enemy = Objects.requireNonNull(enemy, "enemy");
        this.resolver = Objects.requireNonNull(resolver, "resolver");
        this.events = Objects.requireNonNull(events, "events");
    }

    /**
     * L'eroe attacca con un colpo normale.
     */
    void heroAttack() {
        playHeroTurn(() -> resolver.hit(hero, enemy, HitModifier.NORMAL));
    }

    /**
     * L'eroe usa la sua abilità speciale, che poi entra in ricarica.
     *
     * @throws IllegalStateException se l'abilità è ancora in ricarica
     */
    void heroUseAbility() {
        if (abilityCooldownLeft > 0) {
            throw new IllegalStateException("Abilità in ricarica per altri " + abilityCooldownLeft + " turni");
        }
        Ability ability = hero.ability();
        playHeroTurn(() -> {
            events.publish(new GameEvent.AbilityUsed(hero, ability.name()));
            ability.use(hero, enemy, resolver);
            abilityCooldownLeft = ability.cooldownTurns();
        });
    }

    /**
     * L'eroe usa un oggetto consumabile del suo inventario, che viene rimosso.
     *
     * @param item oggetto da usare
     * @throws IllegalArgumentException se l'oggetto non è nell'inventario
     */
    void heroUseItem(Consumable item) {
        if (!hero.inventory().contains(item)) {
            throw new IllegalArgumentException("Oggetto non presente nell'inventario: " + item);
        }
        playHeroTurn(() -> {
            hero.inventory().remove(item);
            item.consume(hero);
            events.publish(new GameEvent.ItemUsed(hero, item));
        });
    }

    /**
     * Restituisce il nemico affrontato.
     *
     * @return nemico
     */
    Enemy enemy() {
        return enemy;
    }

    /**
     * Restituisce i turni che mancano prima di poter riusare l'abilità.
     *
     * @return turni di ricarica rimasti, 0 se l'abilità è pronta
     */
    int abilityCooldownLeft() {
        return abilityCooldownLeft;
    }

    /**
     * Restituisce lo stato del combattimento.
     *
     * @return esito, oppure {@link BattleOutcome#ONGOING} se non è finito
     */
    BattleOutcome outcome() {
        return outcome;
    }

    private void playHeroTurn(Runnable heroAction) {
        if (outcome != BattleOutcome.ONGOING) {
            throw new IllegalStateException("Il combattimento è già finito");
        }
        if (abilityCooldownLeft > 0) {
            abilityCooldownLeft--;
        }
        heroAction.run();
        if (endIfDefeated(enemy, BattleOutcome.VICTORY)) {
            return;
        }
        enemy.takeTurn(hero, resolver);
        endIfDefeated(hero, BattleOutcome.DEFEAT);
    }

    private boolean endIfDefeated(Combatant combatant, BattleOutcome result) {
        if (combatant.isAlive()) {
            return false;
        }
        outcome = result;
        events.publish(new GameEvent.CombatantDefeated(combatant));
        events.publish(new GameEvent.BattleEnded(result));
        return true;
    }
}
