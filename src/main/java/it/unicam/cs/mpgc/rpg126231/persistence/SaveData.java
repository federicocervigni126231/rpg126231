package it.unicam.cs.mpgc.rpg126231.persistence;

import java.util.List;

/**
 * Forma della partita salvata nel file: solo tipi semplici, e classe dell'eroe
 * e oggetti indicati tramite il loro identificativo.
 *
 * @param hero      dati dell'eroe
 * @param floor     indice del piano
 * @param encounter indice del combattimento nel piano
 */
record SaveData(HeroData hero, int floor, int encounter) {

    /**
     * Dati dell'eroe salvati nel file.
     *
     * @param name             nome
     * @param heroClassId      identificativo della classe
     * @param level            livello
     * @param experience       esperienza verso il prossimo livello
     * @param currentHp        punti vita attuali
     * @param inventoryItemIds identificativi degli oggetti nell'inventario
     * @param equippedWeaponId identificativo dell'arma equipaggiata, {@code null} se nessuna
     */
    record HeroData(String name, String heroClassId, int level, int experience, int currentHp,
                    List<String> inventoryItemIds, String equippedWeaponId) {
    }
}
