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
import java.nio.file.AtomicMoveNotSupportedException;
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
        Path temporary = null;
        try {
            Path directory = file.toAbsolutePath().getParent();
            Files.createDirectories(directory);
            temporary = Files.createTempFile(directory, "save", ".tmp");
            Files.writeString(temporary, json, StandardCharsets.UTF_8);
            replaceSaveWith(temporary);
        } catch (IOException e) {
            PersistenceException failure = new PersistenceException("Impossibile salvare la partita", e);
            deleteLeftover(temporary, failure);
            throw failure;
        }
    }

    @Override
    public Optional<SavedGame> load() {
        if (!exists()) {
            return Optional.empty();
        }
        SaveData data;
        try {
            data = gson.fromJson(Files.readString(file, StandardCharsets.UTF_8), SaveData.class);
        } catch (IOException | JsonParseException e) {
            throw new PersistenceException("Il salvataggio non è leggibile", e);
        }
        if (data == null) {
            throw new PersistenceException("Il salvataggio è vuoto");
        }
        try {
            return Optional.of(mapper.fromData(data));
        } catch (IllegalArgumentException e) {
            throw new PersistenceException("Il salvataggio non è valido", e);
        }
    }

    @Override
    public boolean exists() {
        return Files.isRegularFile(file);
    }

    private void replaceSaveWith(Path temporary) throws IOException {
        try {
            Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            // Alcuni file system non supportano lo spostamento atomico: si ripiega su quello normale.
            Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void deleteLeftover(Path temporary, Exception failure) {
        if (temporary == null) {
            return;
        }
        try {
            Files.deleteIfExists(temporary);
        } catch (IOException e) {
            failure.addSuppressed(e);
        }
    }
}
