package it.unicam.cs.mpgc.rpg126231.ui.javafx;

import javafx.scene.Parent;

/**
 * Schermata dell'interfaccia JavaFX.
 */
interface View {

    /**
     * Restituisce il nodo radice da mostrare nella finestra.
     *
     * @return radice della schermata
     */
    Parent root();

    /**
     * Libera le risorse quando la schermata viene sostituita, per esempio
     * cancellando l'iscrizione agli eventi. Per impostazione predefinita non fa nulla.
     */
    default void dispose() {
    }
}
