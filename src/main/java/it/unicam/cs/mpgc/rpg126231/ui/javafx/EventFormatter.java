package it.unicam.cs.mpgc.rpg126231.ui.javafx;

import it.unicam.cs.mpgc.rpg126231.model.combat.BattleOutcome;
import it.unicam.cs.mpgc.rpg126231.service.event.GameEvent;

/**
 * Traduce gli eventi di gioco nei messaggi mostrati nel registro della battaglia.
 */
final class EventFormatter {

    private EventFormatter() {
    }

    /**
     * Restituisce il messaggio che descrive un evento.
     *
     * @param event evento di gioco
     * @return messaggio per il giocatore
     */
    static String describe(GameEvent event) {
        return switch (event) {
            case GameEvent.BattleStarted e -> "Piano %d: appare %s!"
                    .formatted(e.progress().floor() + 1, e.enemy().name());
            case GameEvent.DamageDealt e -> "%s colpisce %s: %d %s."
                    .formatted(e.attacker().name(), e.defender().name(), e.amount(), e.amount() == 1 ? "danno" : "danni");
            case GameEvent.AbilityUsed e -> "%s usa %s!".formatted(e.user().name(), e.abilityName());
            case GameEvent.ItemUsed e -> "%s usa %s.".formatted(e.user().name(), e.item().name());
            case GameEvent.CombatantDefeated e -> "%s è sconfitto.".formatted(e.combatant().name());
            case GameEvent.BattleEnded e -> e.outcome() == BattleOutcome.VICTORY ? "Vittoria!" : "Sconfitta...";
            case GameEvent.ExperienceGained e -> "Ottieni %d punti esperienza.".formatted(e.amount());
            case GameEvent.LeveledUp e -> "Sali al livello %d! Punti vita ripristinati.".formatted(e.newLevel());
            case GameEvent.ItemLooted e -> "Trovi: %s.".formatted(e.item().name());
            case GameEvent.GameWon e -> "Hai completato il dungeon. Vittoria!";
            case GameEvent.GameLost e -> "Il tuo eroe è caduto. La partita è finita.";
        };
    }
}
