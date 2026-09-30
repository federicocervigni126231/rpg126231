package it.unicam.cs.mpgc.rpg126231.model.dungeon;

import it.unicam.cs.mpgc.rpg126231.model.enemy.EnemyTemplate;

import java.util.List;
import java.util.Optional;

/**
 * Struttura del dungeon: una sequenza di piani, ciascuno con i suoi combattimenti.
 */
public class Dungeon {

    private final List<Floor> floors;

    /**
     * Crea il dungeon.
     *
     * @param floors piani in ordine, almeno uno
     * @throws IllegalArgumentException se non ci sono piani
     */
    public Dungeon(List<Floor> floors) {
        this.floors = List.copyOf(floors);
        if (this.floors.isEmpty()) {
            throw new IllegalArgumentException("Il dungeon deve avere almeno un piano");
        }
    }

    /**
     * Restituisce il nemico da affrontare nella posizione indicata.
     *
     * @param progress posizione nel dungeon
     * @return modello del nemico da affrontare
     * @throws IllegalArgumentException se la posizione è fuori dal dungeon
     */
    public EnemyTemplate encounterAt(DungeonProgress progress) {
        requireInside(progress);
        return floors.get(progress.floor()).encounters().get(progress.encounter());
    }

    /**
     * Restituisce la posizione successiva: il prossimo combattimento del piano,
     * oppure il primo del piano seguente.
     *
     * @param progress posizione attuale
     * @return posizione successiva, vuota se quella attuale è l'ultimo combattimento
     * @throws IllegalArgumentException se la posizione è fuori dal dungeon
     */
    public Optional<DungeonProgress> next(DungeonProgress progress) {
        requireInside(progress);
        if (progress.encounter() + 1 < floors.get(progress.floor()).encounters().size()) {
            return Optional.of(new DungeonProgress(progress.floor(), progress.encounter() + 1));
        }
        if (progress.floor() + 1 < floors.size()) {
            return Optional.of(new DungeonProgress(progress.floor() + 1, 0));
        }
        return Optional.empty();
    }

    /**
     * Restituisce il numero di piani.
     *
     * @return numero di piani
     */
    public int floorCount() {
        return floors.size();
    }

    /**
     * Indica se la posizione corrisponde a un combattimento di questo dungeon.
     *
     * @param progress posizione da verificare
     * @return {@code true} se la posizione è valida
     */
    public boolean contains(DungeonProgress progress) {
        return progress.floor() < floors.size()
                && progress.encounter() < floors.get(progress.floor()).encounters().size();
    }

    private void requireInside(DungeonProgress progress) {
        if (!contains(progress)) {
            throw new IllegalArgumentException("Posizione fuori dal dungeon: " + progress);
        }
    }
}
