package it.unicam.cs.mpgc.rpg126231.model.hero;

import it.unicam.cs.mpgc.rpg126231.model.ability.Ability;
import it.unicam.cs.mpgc.rpg126231.model.combat.Combatant;
import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;
import it.unicam.cs.mpgc.rpg126231.model.item.Inventory;
import it.unicam.cs.mpgc.rpg126231.model.item.Weapon;

import java.util.Objects;
import java.util.Optional;

/**
 * L'eroe controllato dal giocatore. Le sue statistiche dipendono dalla classe e dal
 * livello; guadagna esperienza, possiede un inventario e può equipaggiare un'arma.
 */
public class Hero extends Combatant {

    /** Esperienza necessaria per livello: dal livello N al successivo servono N volte questo valore. */
    public static final int EXPERIENCE_PER_LEVEL = 100;

    private final HeroClass heroClass;
    private final Inventory inventory;
    private int level;
    private int experience;
    private Weapon equippedWeapon;

    /**
     * Crea un nuovo eroe al livello 1, con punti vita pieni e inventario vuoto.
     *
     * @param name      nome dell'eroe
     * @param heroClass classe dell'eroe
     */
    public Hero(String name, HeroClass heroClass) {
        this(name, heroClass, 1, 0, heroClass.baseStats().maxHp(), new Inventory(), null);
    }

    /**
     * Ricostruisce un eroe con uno stato già esistente, per esempio da un salvataggio.
     *
     * @param name           nome dell'eroe
     * @param heroClass      classe dell'eroe
     * @param level          livello, almeno 1
     * @param experience     esperienza accumulata verso il prossimo livello
     * @param currentHp      punti vita attuali
     * @param inventory      inventario
     * @param equippedWeapon arma equipaggiata, oppure {@code null} se nessuna
     * @throws IllegalArgumentException se livello, esperienza o punti vita non sono validi
     */
    public Hero(String name, HeroClass heroClass, int level, int experience, int currentHp,
                Inventory inventory, Weapon equippedWeapon) {
        super(name, currentHp);
        if (level < 1) {
            throw new IllegalArgumentException("Il livello deve essere almeno 1");
        }
        if (experience < 0 || experience >= level * EXPERIENCE_PER_LEVEL) {
            throw new IllegalArgumentException("Esperienza non valida per il livello " + level);
        }
        this.heroClass = Objects.requireNonNull(heroClass, "heroClass");
        this.inventory = Objects.requireNonNull(inventory, "inventory");
        this.level = level;
        this.experience = experience;
        this.equippedWeapon = equippedWeapon;
        requireValidHp();
    }

    @Override
    public Stats stats() {
        return heroClass.baseStats().plus(heroClass.growthPerLevel().times(level - 1));
    }

    /**
     * Restituisce l'attacco delle statistiche più il bonus dell'arma equipaggiata.
     *
     * @return potenza d'attacco
     */
    @Override
    public int attackPower() {
        return super.attackPower() + equippedWeapon().map(Weapon::attackBonus).orElse(0);
    }

    /**
     * Aggiunge esperienza e fa salire di livello tutte le volte necessarie.
     * Se l'eroe sale di livello, i suoi punti vita tornano al massimo.
     *
     * @param amount esperienza guadagnata, non negativa
     * @return numero di livelli guadagnati
     */
    public int gainExperience(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("L'esperienza guadagnata non può essere negativa");
        }
        experience += amount;
        int levelsGained = 0;
        while (experience >= experienceToNextLevel()) {
            experience -= experienceToNextLevel();
            level++;
            levelsGained++;
        }
        if (levelsGained > 0) {
            restoreFullHp();
        }
        return levelsGained;
    }

    /**
     * Equipaggia un'arma presa dall'inventario. L'arma equipaggiata in precedenza,
     * se presente, torna nell'inventario.
     *
     * @param weapon arma da equipaggiare
     * @throws IllegalArgumentException se l'arma non è nell'inventario
     */
    public void equip(Weapon weapon) {
        inventory.remove(weapon);
        if (equippedWeapon != null) {
            inventory.add(equippedWeapon);
        }
        equippedWeapon = weapon;
    }

    /**
     * Restituisce l'esperienza totale richiesta per passare al livello successivo.
     *
     * @return esperienza richiesta
     */
    public int experienceToNextLevel() {
        return level * EXPERIENCE_PER_LEVEL;
    }

    /**
     * Restituisce l'abilità speciale data dalla classe.
     *
     * @return abilità speciale
     */
    public Ability ability() {
        return heroClass.ability();
    }

    /**
     * Restituisce la classe dell'eroe.
     *
     * @return classe dell'eroe
     */
    public HeroClass heroClass() {
        return heroClass;
    }

    /**
     * Restituisce l'inventario dell'eroe.
     *
     * @return inventario
     */
    public Inventory inventory() {
        return inventory;
    }

    /**
     * Restituisce il livello attuale.
     *
     * @return livello
     */
    public int level() {
        return level;
    }

    /**
     * Restituisce l'esperienza accumulata verso il prossimo livello.
     *
     * @return esperienza attuale
     */
    public int experience() {
        return experience;
    }

    /**
     * Restituisce l'arma equipaggiata, se presente.
     *
     * @return arma equipaggiata
     */
    public Optional<Weapon> equippedWeapon() {
        return Optional.ofNullable(equippedWeapon);
    }
}
