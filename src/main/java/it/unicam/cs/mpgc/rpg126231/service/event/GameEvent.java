package it.unicam.cs.mpgc.rpg126231.service.event;

import it.unicam.cs.mpgc.rpg126231.model.combat.BattleOutcome;
import it.unicam.cs.mpgc.rpg126231.model.combat.Combatant;
import it.unicam.cs.mpgc.rpg126231.model.dungeon.DungeonProgress;
import it.unicam.cs.mpgc.rpg126231.model.enemy.Enemy;
import it.unicam.cs.mpgc.rpg126231.model.item.Item;

/**
 * Qualcosa di rilevante accaduto durante la partita, notificato agli ascoltatori
 * tramite {@link EventBus}. L'interfaccia è {@code sealed}: gli eventi possibili sono
 * solo quelli dichiarati qui, e un ascoltatore può gestirli con uno {@code switch}.
 */
public sealed interface GameEvent {

    /**
     * È iniziato un combattimento.
     *
     * @param enemy    nemico da affrontare
     * @param progress posizione nel dungeon
     */
    record BattleStarted(Enemy enemy, DungeonProgress progress) implements GameEvent {
    }

    /**
     * Un combattente ha colpito un altro.
     *
     * @param attacker chi ha colpito
     * @param defender chi è stato colpito
     * @param amount   punti vita tolti
     */
    record DamageDealt(Combatant attacker, Combatant defender, int amount) implements GameEvent {
    }

    /**
     * Un combattente ha usato la sua abilità speciale.
     *
     * @param user        chi ha usato l'abilità
     * @param abilityName nome dell'abilità
     */
    record AbilityUsed(Combatant user, String abilityName) implements GameEvent {
    }

    /**
     * Un combattente ha usato un oggetto.
     *
     * @param user chi ha usato l'oggetto
     * @param item oggetto usato
     */
    record ItemUsed(Combatant user, Item item) implements GameEvent {
    }

    /**
     * Un combattente è stato sconfitto.
     *
     * @param combatant combattente sconfitto
     */
    record CombatantDefeated(Combatant combatant) implements GameEvent {
    }

    /**
     * Il combattimento è finito.
     *
     * @param outcome esito del combattimento
     */
    record BattleEnded(BattleOutcome outcome) implements GameEvent {
    }

    /**
     * L'eroe ha guadagnato esperienza.
     *
     * @param amount esperienza guadagnata
     */
    record ExperienceGained(int amount) implements GameEvent {
    }

    /**
     * L'eroe è salito di livello.
     *
     * @param newLevel nuovo livello
     */
    record LeveledUp(int newLevel) implements GameEvent {
    }

    /**
     * L'eroe ha ottenuto un oggetto da un nemico sconfitto.
     *
     * @param item oggetto ottenuto
     */
    record ItemLooted(Item item) implements GameEvent {
    }

    /**
     * L'eroe ha completato il dungeon.
     */
    record GameWon() implements GameEvent {
    }

    /**
     * L'eroe è stato sconfitto e la partita è finita.
     */
    record GameLost() implements GameEvent {
    }
}
