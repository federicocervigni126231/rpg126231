package it.unicam.cs.mpgc.rpg126231.ui.javafx;

import it.unicam.cs.mpgc.rpg126231.app.GameBootstrap;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Punto di ingresso dell'interfaccia grafica JavaFX: collega i componenti tramite
 * {@link GameBootstrap} e apre il menu principale.
 */
public class RpgApplication extends Application {

    private static final String TITLE = "Dungeon RPG";
    private static final double WIDTH = 900;
    private static final double HEIGHT = 640;
    private static final String STYLESHEET = "style.css";

    /**
     * Avvia l'applicazione JavaFX.
     *
     * @param args argomenti da riga di comando
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        GameBootstrap bootstrap = new GameBootstrap(GameBootstrap.defaultSaveFile());
        Scene scene = new Scene(new Pane(), WIDTH, HEIGHT);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(STYLESHEET), STYLESHEET).toExternalForm());
        new Navigator(scene, bootstrap.gameService(), bootstrap.events()).showMainMenu();

        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }
}
