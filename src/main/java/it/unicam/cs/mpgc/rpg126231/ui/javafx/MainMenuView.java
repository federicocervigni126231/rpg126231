package it.unicam.cs.mpgc.rpg126231.ui.javafx;

import it.unicam.cs.mpgc.rpg126231.service.GameService;
import it.unicam.cs.mpgc.rpg126231.service.PersistenceException;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Menu principale: nuova partita, caricamento della partita salvata, uscita.
 */
class MainMenuView implements View {

    private final Navigator navigator;
    private final GameService gameService;
    private final VBox root = new VBox(16);

    /**
     * Crea il menu.
     *
     * @param navigator   navigatore tra le schermate
     * @param gameService servizio di gioco
     */
    MainMenuView(Navigator navigator, GameService gameService) {
        this.navigator = navigator;
        this.gameService = gameService;
        build();
    }

    @Override
    public Parent root() {
        return root;
    }

    private void build() {
        Label title = new Label("Dungeon RPG");
        title.getStyleClass().add("title");

        Button newGame = menuButton("Nuova partita");
        newGame.setOnAction(e -> navigator.showCharacterCreation());

        Button loadGame = menuButton("Carica partita");
        loadGame.setDisable(!gameService.hasSavedGame());
        loadGame.setOnAction(e -> loadSavedGame());

        Button exit = menuButton("Esci");
        exit.setOnAction(e -> Platform.exit());

        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(title, newGame, loadGame, exit);
    }

    private void loadSavedGame() {
        try {
            gameService.loadGame().ifPresent(navigator::showBattle);
        } catch (PersistenceException e) {
            Dialogs.showError("Impossibile caricare la partita: " + e.getMessage());
        }
    }

    private static Button menuButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("menu-button");
        return button;
    }
}
