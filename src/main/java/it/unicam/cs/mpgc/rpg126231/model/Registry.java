package it.unicam.cs.mpgc.rpg126231.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Catalogo di elementi recuperabili tramite il loro identificativo.
 * Permette di aggiungere nuove classi di eroe o nuovi oggetti registrandoli,
 * senza modificare il codice che li usa.
 *
 * @param <T> tipo degli elementi registrati
 */
public class Registry<T extends Identifiable> {

    private final Map<String, T> elements = new LinkedHashMap<>();

    /**
     * Registra un elemento.
     *
     * @param element elemento da registrare
     * @throws IllegalArgumentException se esiste già un elemento con lo stesso identificativo
     */
    public void register(T element) {
        Objects.requireNonNull(element, "element");
        if (elements.putIfAbsent(element.id(), element) != null) {
            throw new IllegalArgumentException("Identificativo già registrato: " + element.id());
        }
    }

    /**
     * Restituisce l'elemento con l'identificativo indicato.
     *
     * @param id identificativo cercato
     * @return l'elemento registrato con quell'identificativo
     * @throws IllegalArgumentException se l'identificativo non è registrato
     */
    public T get(String id) {
        T element = elements.get(id);
        if (element == null) {
            throw new IllegalArgumentException("Identificativo sconosciuto: " + id);
        }
        return element;
    }

    /**
     * Restituisce tutti gli elementi, nell'ordine di registrazione.
     *
     * @return vista non modificabile degli elementi registrati
     */
    public Collection<T> all() {
        return Collections.unmodifiableCollection(elements.values());
    }
}
