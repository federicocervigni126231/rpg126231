package it.unicam.cs.mpgc.rpg126231.service.event;

/**
 * Ascoltatore degli eventi di gioco (pattern Observer). Un'interfaccia grafica,
 * desktop o web, riceve gli eventi implementando questa interfaccia.
 */
@FunctionalInterface
public interface GameEventListener {

    /**
     * Riceve un evento di gioco.
     *
     * @param event evento accaduto
     */
    void onEvent(GameEvent event);
}
