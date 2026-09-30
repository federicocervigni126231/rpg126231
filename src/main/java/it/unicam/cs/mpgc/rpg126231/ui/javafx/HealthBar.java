package it.unicam.cs.mpgc.rpg126231.ui.javafx;

import it.unicam.cs.mpgc.rpg126231.model.combat.Combatant;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Barra dei punti vita di un combattente, con il valore numerico.
 */
class HealthBar extends VBox {

    private final ProgressBar bar = new ProgressBar();
    private final Label value = new Label();

    /**
     * Crea una barra vuota.
     */
    HealthBar() {
        super(4);
        bar.setMaxWidth(Double.MAX_VALUE);
        bar.setMinHeight(Region.USE_PREF_SIZE);
        getStyleClass().add("health-bar");
        getChildren().addAll(bar, value);
    }

    /**
     * Aggiorna la barra con i punti vita del combattente.
     *
     * @param combatant combattente da mostrare
     */
    void show(Combatant combatant) {
        bar.setProgress((double) combatant.currentHp() / combatant.maxHp());
        value.setText("PV %d / %d".formatted(combatant.currentHp(), combatant.maxHp()));
    }
}
