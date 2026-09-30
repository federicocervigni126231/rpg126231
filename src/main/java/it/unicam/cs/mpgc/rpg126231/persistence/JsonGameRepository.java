package it.unicam.cs.mpgc.rpg126231.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import it.unicam.cs.mpgc.rpg126231.model.Registry;
import it.unicam.cs.mpgc.rpg126231.model.hero.HeroClass;
import it.unicam.cs.mpgc.rpg126231.model.item.Item;
import it.unicam.cs.mpgc.rpg126231.service.GameRepository;
import it.unicam.cs.mpgc.rpg126231.service.PersistenceException;
import it.unicam.cs.mpgc.rpg126231.service.SavedGame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

/**
 * Salva la partita in un file JSON. Scrive prima un file temporaneo e poi lo sostituisce
 * a quello vecchio, così un'interruzione durante la scrittura non rovina il salvataggio.
 */
public class JsonGameRepository implements GameRepository {

    private final Path file;
    private final SaveDataMapper mapper;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Crea il repository.
     *
     * @param file        percorso del file di salvataggio
     * @param heroClasses registro delle classi di eroe, per ricostruire l'eroe
     * @param items       registro degli oggetti, per ricostruire l'inventario
     */
    public JsonGameRepository(Path file, Registry<HeroClass> heroClasses, Registry<Item> items) {
        this.file = Objects.requireNonNull(file, "file");
        this.mapper = new SaveDataMapper(heroClasses, items);
    }

    @Override
    public void save(SavedGame game) {
        String json = gson.toJson(mapper.toData(game));
        try {
            Path directory = file.toAbsolutePath().getParent();
            Files.createDirectories(directory);
            Path temporary = Files.createTempFile(directory, "save", ".tmp");
            Files.writeString(temporary, json, StandardCharsets.UTF_8);
            Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new PersistenceException("Impossibile salvare la partita", e);
        }
    }

    @Override
    public Optional<SavedGame> load() {
        if (!exists()) {
            return Optional.empty();
        }
        try {
            SaveData data = gson.fromJson(Files.readString(file, StandardCharsets.UTF_8), SaveData.class);
            return Optional.of(mapper.fromData(Objects.requireNonNull(data, "file vuoto")));
        } catch (IOException | JsonParseException | IllegalArgumentException | NullPointerException e) {
            throw new PersistenceException("Il salvataggio non è leggibile", e);
        }
    }

    @Override
    public boolean exists() {
        return Files.isRegularFile(file);
    }
}
