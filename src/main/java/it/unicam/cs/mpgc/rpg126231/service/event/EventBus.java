package it.unicam.cs.mpgc.rpg126231.service.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Canale degli eventi di gioco: chi produce eventi li pubblica qui, senza sapere
 * chi li ascolta; gli ascoltatori iscritti li ricevono nell'ordine di iscrizione.
 */
public class EventBus {

    private final List<GameEventListener> listeners = new ArrayList<>();

    /**
     * Iscrive un ascoltatore.
     *
     * @param listener ascoltatore da iscrivere
     */
    public void subscribe(GameEventListener listener) {
        listeners.add(Objects.requireNonNull(listener, "listener"));
    }

    /**
     * Cancella l'iscrizione di un ascoltatore, per esempio quando una schermata viene chiusa.
     *
     * @param listener ascoltatore da rimuovere
     */
    public void unsubscribe(GameEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifica un evento a tutti gli ascoltatori iscritti.
     *
     * @param event evento da notificare
     */
    public void publish(GameEvent event) {
        Objects.requireNonNull(event, "event");
        for (GameEventListener listener : List.copyOf(listeners)) {
            listener.onEvent(event);
        }
    }
}
