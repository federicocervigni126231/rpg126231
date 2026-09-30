package it.unicam.cs.mpgc.rpg126231.model.enemy;

import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;
import it.unicam.cs.mpgc.rpg126231.model.item.Item;

import java.util.List;

/**
 * Descrizione di un tipo di nemico, usata come fabbrica di nemici nuovi.
 * Un nuovo tipo di nemico si aggiunge creando un nuovo modello, senza nuove classi.
 *
 * @param name             nome del nemico
 * @param stats            statistiche
 * @param experienceReward esperienza data se sconfitto
 * @param behavior         comportamento in combattimento
 * @param possibleDrops    oggetti che può lasciare se sconfitto
 */
public record EnemyTemplate(String name, Stats stats, int experienceReward, EnemyBehavior behavior,
                            List<Item> possibleDrops) {

    /**
     * Crea il modello copiando la lista degli oggetti.
     */
    public EnemyTemplate {
        possibleDrops = List.copyOf(possibleDrops);
    }

    /**
     * Crea un nuovo nemico di questo tipo, con punti vita pieni.
     *
     * @return nuovo nemico
     */
    public Enemy spawn() {
        return new Enemy(name, stats, experienceReward, behavior, possibleDrops);
    }
}
