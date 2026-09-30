package it.unicam.cs.mpgc.rpg126231.ui.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Punto di ingresso dell'interfaccia grafica JavaFX.
 */
public class RpgApplication extends Application {

    private static final String TITLE = "Dungeon RPG";
    private static final double WIDTH = 800;
    private static final double HEIGHT = 600;

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
        stage.setTitle(TITLE);
        stage.setScene(new Scene(new StackPane(new Label(TITLE)), WIDTH, HEIGHT));
        stage.show();
    }
}
