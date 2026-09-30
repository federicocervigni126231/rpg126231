package it.unicam.cs.mpgc.rpg126231.ui.javafx;

import javafx.scene.control.Alert;

/**
 * Finestre di dialogo comuni alle schermate.
 */
final class Dialogs {

    private Dialogs() {
    }

    /**
     * Mostra un messaggio di errore e attende che il giocatore lo chiuda.
     *
     * @param message testo dell'errore
     */
    static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
