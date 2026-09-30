package it.unicam.cs.mpgc.rpg126231.service;

import it.unicam.cs.mpgc.rpg126231.model.Registry;
import it.unicam.cs.mpgc.rpg126231.model.combat.DamageResolver;
import it.unicam.cs.mpgc.rpg126231.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg126231.model.dungeon.DungeonProgress;
import it.unicam.cs.mpgc.rpg126231.model.hero.Hero;
import it.unicam.cs.mpgc.rpg126231.model.hero.HeroClass;
import it.unicam.cs.mpgc.rpg126231.service.event.EventBus;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * Operazioni fuori dalla partita: elenco delle classi di eroe, nuova partita,
 * salvataggio e caricamento. Crea le {@link GameSession} già collegate a tutto ciò
 * che serve, così l'interfaccia non deve conoscere i dettagli di costruzione.
 */
public class GameService {

    private final Registry<HeroClass> heroClasses;
    private final Dungeon dungeon;
    private final GameRepository repository;
    private final DamageResolver resolver;
    private final LootGenerator lootGenerator;
    private final EventBus events;

    /**
     * Crea il servizio.
     *
     * @param heroClasses   classi di eroe disponibili
     * @param dungeon       dungeon da attraversare in ogni partita
     * @param repository    archivio della partita salvata
     * @param resolver      risolutore dei colpi
     * @param lootGenerator generatore degli oggetti lasciati dai nemici
     * @param events        canale degli eventi
     */
    public GameService(Registry<HeroClass> heroClasses, Dungeon dungeon, GameRepository repository,
                       DamageResolver resolver, LootGenerator lootGenerator, EventBus events) {
        this.heroClasses = Objects.requireNonNull(heroClasses, "heroClasses");
        this.dungeon = Objects.requireNonNull(dungeon, "dungeon");
        this.repository = Objects.requireNonNull(repository, "repository");
        this.resolver = Objects.requireNonNull(resolver, "resolver");
        this.lootGenerator = Objects.requireNonNull(lootGenerator, "lootGenerator");
        this.events = Objects.requireNonNull(events, "events");
    }

    /**
     * Restituisce le classi di eroe tra cui il giocatore può scegliere.
     *
     * @return classi disponibili, nell'ordine di registrazione
     */
    public Collection<HeroClass> availableHeroClasses() {
        return heroClasses.all();
    }

    /**
     * Inizia una nuova partita dal primo combattimento del dungeon.
     *
     * @param heroName    nome dell'eroe
     * @param heroClassId identificativo della classe scelta
     * @return nuova partita
     * @throws IllegalArgumentException se il nome è vuoto o la classe non esiste
     */
    public GameSession newGame(String heroName, String heroClassId) {
        return createSession(new Hero(heroName, heroClasses.get(heroClassId)), DungeonProgress.START);
    }

    /**
     * Riprende la partita salvata.
     *
     * @return partita caricata, vuota se non c'è un salvataggio
     * @throws PersistenceException se il salvataggio non si può leggere o non corrisponde al dungeon
     */
    public Optional<GameSession> loadGame() {
        return repository.load().map(saved -> {
            if (!dungeon.contains(saved.progress())) {
                throw new PersistenceException("Il salvataggio non corrisponde al dungeon: " + saved.progress());
            }
            return createSession(saved.hero(), saved.progress());
        });
    }

    /**
     * Salva la partita. È possibile solo tra un combattimento e l'altro.
     *
     * @param session partita da salvare
     * @throws IllegalStateException se è in corso un combattimento o la partita è finita
     * @throws PersistenceException  se il salvataggio non riesce
     */
    public void saveGame(GameSession session) {
        if (session.isInBattle() || session.isOver()) {
            throw new IllegalStateException("Si può salvare solo tra un combattimento e l'altro");
        }
        repository.save(new SavedGame(session.hero(), session.progress()));
    }

    /**
     * Indica se esiste una partita salvata.
     *
     * @return {@code true} se esiste un salvataggio
     */
    public boolean hasSavedGame() {
        return repository.exists();
    }

    private GameSession createSession(Hero hero, DungeonProgress progress) {
        return new GameSession(hero, dungeon, progress, resolver, lootGenerator, events);
    }
}
