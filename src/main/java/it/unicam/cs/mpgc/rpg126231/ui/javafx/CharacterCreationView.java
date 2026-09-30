package it.unicam.cs.mpgc.rpg126231.ui.javafx;

import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;
import it.unicam.cs.mpgc.rpg126231.model.hero.HeroClass;
import it.unicam.cs.mpgc.rpg126231.service.GameService;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Creazione del personaggio: nome dell'eroe e scelta della classe. Le classi mostrate
 * sono quelle registrate, quindi una nuova classe compare qui senza modifiche.
 */
class CharacterCreationView implements View {

    private final Navigator navigator;
    private final GameService gameService;
    private final VBox root = new VBox(16);
    private final TextField nameField = new TextField();
    private final ToggleGroup classGroup = new ToggleGroup();

    /**
     * Crea la schermata.
     *
     * @param navigator   navigatore tra le schermate
     * @param gameService servizio di gioco
     */
    CharacterCreationView(Navigator navigator, GameService gameService) {
        this.navigator = navigator;
        this.gameService = gameService;
        build();
    }

    @Override
    public Parent root() {
        return root;
    }

    private void build() {
        Label title = new Label("Crea il tuo eroe");
        title.getStyleClass().add("title");

        nameField.setPromptText("Nome dell'eroe");
        nameField.setMaxWidth(320);

        VBox classes = new VBox(8);
        classes.setMaxWidth(420);
        for (HeroClass heroClass : gameService.availableHeroClasses()) {
            classes.getChildren().add(classOption(heroClass));
        }
        classGroup.getToggles().get(0).setSelected(true);

        Button start = new Button("Inizia l'avventura");
        start.setDefaultButton(true);
        start.disableProperty().bind(Bindings.createBooleanBinding(
                () -> nameField.getText().isBlank(), nameField.textProperty()));
        start.setOnAction(e -> startGame());

        Button back = new Button("Indietro");
        back.setCancelButton(true);
        back.setOnAction(e -> navigator.showMainMenu());

        HBox buttons = new HBox(12, back, start);
        buttons.setAlignment(Pos.CENTER);

        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(title, nameField, new Label("Scegli la classe:"), classes, buttons);
    }

    private RadioButton classOption(HeroClass heroClass) {
        Stats stats = heroClass.baseStats();
        RadioButton option = new RadioButton("%s  ·  PV %d  ATT %d  DIF %d  ·  Abilità: %s".formatted(
                heroClass.displayName(), stats.maxHp(), stats.attack(), stats.defense(),
                heroClass.ability().name()));
        option.setUserData(heroClass);
        option.setToggleGroup(classGroup);
        return option;
    }

    private void startGame() {
        HeroClass heroClass = (HeroClass) classGroup.getSelectedToggle().getUserData();
        navigator.showBattle(gameService.newGame(nameField.getText().strip(), heroClass.id()));
    }
}
