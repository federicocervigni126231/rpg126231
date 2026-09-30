package it.unicam.cs.mpgc.rpg126231.app;

import it.unicam.cs.mpgc.rpg126231.model.Registry;
import it.unicam.cs.mpgc.rpg126231.model.hero.HeroClass;
import it.unicam.cs.mpgc.rpg126231.persistence.JsonGameRepository;
import it.unicam.cs.mpgc.rpg126231.service.DamageCalculator;
import it.unicam.cs.mpgc.rpg126231.service.GameService;
import it.unicam.cs.mpgc.rpg126231.service.LootGenerator;
import it.unicam.cs.mpgc.rpg126231.service.event.EventBus;

import java.nio.file.Path;
import java.util.random.RandomGenerator;

/**
 * Punto in cui si creano e si collegano gli oggetti dell'applicazione (composition root).
 * Qualunque interfaccia, desktop o web, lo usa per ottenere il servizio di gioco
 * e il canale degli eventi, senza sapere come sono costruiti.
 */
public class GameBootstrap {

    private static final int DROP_CHANCE_PERCENT = 50;

    private final EventBus events = new EventBus();
    private final GameService gameService;

    /**
     * Collega tutti i componenti, salvando le partite nel file indicato.
     *
     * @param saveFile percorso del file di salvataggio
     */
    public GameBootstrap(Path saveFile) {
        RandomGenerator random = RandomGenerator.getDefault();
        Registry<HeroClass> heroClasses = GameContent.heroClasses();
        gameService = new GameService(
                heroClasses,
                GameContent.dungeon(),
                new JsonGameRepository(saveFile, heroClasses, GameContent.items()),
                new DamageCalculator(random, events),
                new LootGenerator(random, DROP_CHANCE_PERCENT),
                events);
    }

    /**
     * Restituisce il percorso predefinito del salvataggio, nella cartella dell'utente.
     *
     * @return percorso {@code ~/.rpg126231/save.json}
     */
    public static Path defaultSaveFile() {
        return Path.of(System.getProperty("user.home"), ".rpg126231", "save.json");
    }

    /**
     * Restituisce il servizio di gioco.
     *
     * @return servizio di gioco
     */
    public GameService gameService() {
        return gameService;
    }

    /**
     * Restituisce il canale degli eventi a cui l'interfaccia si iscrive.
     *
     * @return canale degli eventi
     */
    public EventBus events() {
        return events;
    }
}
