package it.unicam.cs.mpgc.rpg126231.service;

import it.unicam.cs.mpgc.rpg126231.model.combat.DamageResolver;
import it.unicam.cs.mpgc.rpg126231.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg126231.model.dungeon.DungeonProgress;
import it.unicam.cs.mpgc.rpg126231.model.enemy.Enemy;
import it.unicam.cs.mpgc.rpg126231.model.hero.Hero;
import it.unicam.cs.mpgc.rpg126231.model.item.Consumable;
import it.unicam.cs.mpgc.rpg126231.service.event.EventBus;
import it.unicam.cs.mpgc.rpg126231.service.event.GameEvent;

import java.util.Objects;
import java.util.Optional;

/**
 * Partita in corso e unico punto d'accesso per l'interfaccia (pattern Facade).
 * Avvia i combattimenti del dungeon uno dopo l'altro, inoltra le azioni dell'eroe,
 * assegna esperienza e oggetti dopo ogni vittoria e stabilisce la fine della partita.
 */
public class GameSession {

    private final Hero hero;
    private final Dungeon dungeon;
    private final DamageResolver resolver;
    private final LootGenerator lootGenerator;
    private final EventBus events;
    private DungeonProgress progress;
    private Battle battle;
    private boolean over;

    /**
     * Crea una partita a partire dalla posizione indicata del dungeon.
     *
     * @param hero          eroe del giocatore
     * @param dungeon       dungeon da attraversare
     * @param progress      posizione da cui partire
     * @param resolver      risolutore dei colpi
     * @param lootGenerator generatore degli oggetti lasciati dai nemici
     * @param events        canale degli eventi
     * @throws IllegalArgumentException se la posizione è fuori dal dungeon
     */
    public GameSession(Hero hero, Dungeon dungeon, DungeonProgress progress, DamageResolver resolver,
                       LootGenerator lootGenerator, EventBus events) {
        if (!dungeon.contains(progress)) {
            throw new IllegalArgumentException("Posizione fuori dal dungeon: " + progress);
        }
        this.hero = Objects.requireNonNull(hero, "hero");
        this.dungeon = dungeon;
        this.progress = progress;
        this.resolver = Objects.requireNonNull(resolver, "resolver");
        this.lootGenerator = Objects.requireNonNull(lootGenerator, "lootGenerator");
        this.events = Objects.requireNonNull(events, "events");
    }

    /**
     * Avvia il combattimento della posizione attuale nel dungeon.
     *
     * @throws IllegalStateException se un combattimento è già in corso o la partita è finita
     */
    public void startNextBattle() {
        if (over) {
            throw new IllegalStateException("La partita è finita");
        }
        if (battle != null) {
            throw new IllegalStateException("Un combattimento è già in corso");
        }
        Enemy enemy = dungeon.encounterAt(progress).spawn();
        battle = new Battle(hero, enemy, resolver, events);
        events.publish(new GameEvent.BattleStarted(enemy, progress));
    }

    /**
     * L'eroe attacca il nemico.
     *
     * @throws IllegalStateException se non c'è un combattimento in corso
     */
    public void attack() {
        currentBattle().heroAttack();
        handleBattleEnd();
    }

    /**
     * L'eroe usa la sua abilità speciale.
     *
     * @throws IllegalStateException se non c'è un combattimento in corso o l'abilità è in ricarica
     */
    public void useAbility() {
        currentBattle().heroUseAbility();
        handleBattleEnd();
    }

    /**
     * L'eroe usa un oggetto consumabile durante il combattimento.
     *
     * @param item oggetto da usare
     * @throws IllegalStateException    se non c'è un combattimento in corso
     * @throws IllegalArgumentException se l'oggetto non è nell'inventario
     */
    public void useItem(Consumable item) {
        currentBattle().heroUseItem(item);
        handleBattleEnd();
    }

    /**
     * Restituisce il nemico del combattimento in corso.
     *
     * @return nemico, vuoto se non c'è un combattimento in corso
     */
    public Optional<Enemy> currentEnemy() {
        return Optional.ofNullable(battle).map(Battle::enemy);
    }

    /**
     * Restituisce i turni che mancano prima di poter riusare l'abilità.
     *
     * @return turni di ricarica rimasti, 0 se non c'è un combattimento in corso
     */
    public int abilityCooldownLeft() {
        return battle == null ? 0 : battle.abilityCooldownLeft();
    }

    /**
     * Indica se un combattimento è in corso.
     *
     * @return {@code true} durante un combattimento
     */
    public boolean isInBattle() {
        return battle != null;
    }

    /**
     * Indica se la partita è finita, con una vittoria o una sconfitta.
     *
     * @return {@code true} se la partita è finita
     */
    public boolean isOver() {
        return over;
    }

    /**
     * Restituisce l'eroe del giocatore.
     *
     * @return eroe
     */
    public Hero hero() {
        return hero;
    }

    /**
     * Restituisce la posizione attuale nel dungeon: il prossimo combattimento da affrontare.
     *
     * @return posizione attuale
     */
    public DungeonProgress progress() {
        return progress;
    }

    /**
     * Restituisce il numero di piani del dungeon.
     *
     * @return numero di piani
     */
    public int floorCount() {
        return dungeon.floorCount();
    }

    private Battle currentBattle() {
        if (battle == null) {
            throw new IllegalStateException("Nessun combattimento in corso");
        }
        return battle;
    }

    private void handleBattleEnd() {
        switch (battle.outcome()) {
            case ONGOING -> {
                return;
            }
            case VICTORY -> rewardVictory(battle.enemy());
            case DEFEAT -> endGame(new GameEvent.GameLost());
        }
        battle = null;
    }

    private void rewardVictory(Enemy enemy) {
        int experience = enemy.experienceReward();
        events.publish(new GameEvent.ExperienceGained(experience));
        if (hero.gainExperience(experience) > 0) {
            events.publish(new GameEvent.LeveledUp(hero.level()));
        }
        lootGenerator.roll(enemy).ifPresent(item -> {
            hero.inventory().add(item);
            events.publish(new GameEvent.ItemLooted(item));
        });
        dungeon.next(progress).ifPresentOrElse(
                next -> progress = next,
                () -> endGame(new GameEvent.GameWon()));
    }

    private void endGame(GameEvent finalEvent) {
        over = true;
        events.publish(finalEvent);
    }
}
