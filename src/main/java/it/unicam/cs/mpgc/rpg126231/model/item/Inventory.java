package it.unicam.cs.mpgc.rpg126231.model.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Insieme ordinato degli oggetti posseduti dall'eroe.
 */
public class Inventory {

    private final List<Item> items = new ArrayList<>();

    /**
     * Aggiunge un oggetto.
     *
     * @param item oggetto da aggiungere
     */
    public void add(Item item) {
        items.add(Objects.requireNonNull(item, "item"));
    }

    /**
     * Rimuove un esemplare dell'oggetto indicato.
     *
     * @param item oggetto da rimuovere
     * @throws IllegalArgumentException se l'oggetto non è nell'inventario
     */
    public void remove(Item item) {
        if (!items.remove(item)) {
            throw new IllegalArgumentException("Oggetto non presente nell'inventario: " + item);
        }
    }

    /**
     * Indica se l'inventario contiene l'oggetto indicato.
     *
     * @param item oggetto cercato
     * @return {@code true} se ne è presente almeno un esemplare
     */
    public boolean contains(Item item) {
        return items.contains(item);
    }

    /**
     * Restituisce tutti gli oggetti, nell'ordine in cui sono stati aggiunti.
     *
     * @return vista non modificabile degli oggetti
     */
    public List<Item> items() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Restituisce gli oggetti di un certo tipo, per esempio solo i consumabili.
     *
     * @param type tipo cercato
     * @param <T>  tipo degli oggetti restituiti
     * @return nuova lista con gli oggetti di quel tipo
     */
    public <T extends Item> List<T> itemsOfType(Class<T> type) {
        return items.stream().filter(type::isInstance).map(type::cast).toList();
    }
}
