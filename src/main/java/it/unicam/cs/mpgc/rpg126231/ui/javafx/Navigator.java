package it.unicam.cs.mpgc.rpg126231.ui.javafx;

import it.unicam.cs.mpgc.rpg126231.service.GameService;
import it.unicam.cs.mpgc.rpg126231.service.GameSession;
import it.unicam.cs.mpgc.rpg126231.service.event.EventBus;
import javafx.scene.Scene;

import java.util.Objects;

/**
 * Crea le schermate e le mostra nella finestra, una alla volta. Le schermate chiedono
 * al navigatore di passare alla successiva, senza sapere come costruirla.
 */
class Navigator {

    private final Scene scene;
    private final GameService gameService;
    private final EventBus events;
    private View current;

    /**
     * Crea il navigatore.
     *
     * @param scene       scena della finestra in cui mostrare le schermate
     * @param gameService servizio di gioco
     * @param events      canale degli eventi
     */
    Navigator(Scene scene, GameService gameService, EventBus events) {
        this.scene = Objects.requireNonNull(scene, "scene");
        this.gameService = Objects.requireNonNull(gameService, "gameService");
        this.events = Objects.requireNonNull(events, "events");
    }

    /**
     * Mostra il menu principale.
     */
    void showMainMenu() {
        show(new MainMenuView(this, gameService));
    }

    /**
     * Mostra la creazione del personaggio.
     */
    void showCharacterCreation() {
        show(new CharacterCreationView(this, gameService));
    }

    /**
     * Mostra la schermata di combattimento della partita.
     *
     * @param session partita in corso
     */
    void showBattle(GameSession session) {
        show(new BattleView(this, gameService, session, events));
    }

    /**
     * Mostra l'inventario dell'eroe.
     *
     * @param session partita in corso
     */
    void showInventory(GameSession session) {
        show(new InventoryView(this, session));
    }

    private void show(View next) {
        if (current != null) {
            current.dispose();
        }
        current = next;
        scene.setRoot(next.root());
    }
}
