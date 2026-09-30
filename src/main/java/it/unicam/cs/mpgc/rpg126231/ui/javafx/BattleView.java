package it.unicam.cs.mpgc.rpg126231.ui.javafx;

import it.unicam.cs.mpgc.rpg126231.model.enemy.Enemy;
import it.unicam.cs.mpgc.rpg126231.model.hero.Hero;
import it.unicam.cs.mpgc.rpg126231.model.item.Consumable;
import it.unicam.cs.mpgc.rpg126231.model.item.Item;
import it.unicam.cs.mpgc.rpg126231.service.GameService;
import it.unicam.cs.mpgc.rpg126231.service.GameSession;
import it.unicam.cs.mpgc.rpg126231.service.PersistenceException;
import it.unicam.cs.mpgc.rpg126231.service.event.EventBus;
import it.unicam.cs.mpgc.rpg126231.service.event.GameEvent;
import it.unicam.cs.mpgc.rpg126231.service.event.GameEventListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.Optional;

/**
 * Schermata principale della partita. Mostra eroe e nemico, il registro degli eventi
 * e, a seconda dello stato, le azioni di combattimento, le scelte tra un combattimento
 * e l'altro oppure la fine della partita. Si aggiorna ascoltando gli eventi di gioco.
 */
class BattleView implements View, GameEventListener {

    private final Navigator navigator;
    private final GameService gameService;
    private final GameSession session;
    private final EventBus events;

    private final BorderPane root = new BorderPane();
    private final Label positionLabel = new Label();
    private final Label heroName = new Label();
    private final Label heroDetails = new Label();
    private final Label heroEquipment = new Label();
    private final HealthBar heroHealth = new HealthBar();
    private final Label enemyName = new Label();
    private final HealthBar enemyHealth = new HealthBar();
    private final ListView<String> log = new ListView<>();

    private final Button abilityButton = new Button();
    private final ComboBox<Consumable> itemChoice = new ComboBox<>();
    private final Button useItemButton = new Button("Usa oggetto");
    private final HBox battleActions = new HBox(10);
    private final HBox betweenBattlesActions = new HBox(10);
    private final HBox gameOverActions = new HBox(10);
    private final Label gameOverMessage = new Label();

    /**
     * Crea la schermata e la iscrive agli eventi di gioco.
     *
     * @param navigator   navigatore tra le schermate
     * @param gameService servizio di gioco, per il salvataggio
     * @param session     partita in corso
     * @param events      canale degli eventi
     */
    BattleView(Navigator navigator, GameService gameService, GameSession session, EventBus events) {
        this.navigator = navigator;
        this.gameService = gameService;
        this.session = session;
        this.events = events;
        build();
        events.subscribe(this);
        if (!session.isOver()) {
            addToLog("Pronto per il prossimo combattimento.");
        }
        refresh();
    }

    @Override
    public Parent root() {
        return root;
    }

    @Override
    public void dispose() {
        events.unsubscribe(this);
    }

    @Override
    public void onEvent(GameEvent event) {
        addToLog(EventFormatter.describe(event));
        refresh();
    }

    private void build() {
        positionLabel.getStyleClass().add("subtitle");
        heroName.getStyleClass().add("name");
        enemyName.getStyleClass().add("name");

        VBox heroPanel = panel(heroName, heroDetails, heroEquipment, heroHealth);
        VBox enemyPanel = panel(enemyName, enemyHealth);
        HBox fighters = new HBox(20, heroPanel, enemyPanel);
        fighters.setMinHeight(Region.USE_PREF_SIZE);
        for (VBox fighterPanel : new VBox[]{heroPanel, enemyPanel}) {
            fighterPanel.setPrefWidth(0);
            HBox.setHgrow(fighterPanel, Priority.ALWAYS);
        }

        VBox.setVgrow(log, Priority.ALWAYS);
        VBox center = new VBox(12, fighters, log);
        center.setPadding(new Insets(12, 0, 12, 0));

        buildBattleActions();
        buildBetweenBattlesActions();
        buildGameOverActions();
        VBox actions = new VBox(battleActions, betweenBattlesActions, gameOverActions);

        root.setPadding(new Insets(20));
        root.setTop(positionLabel);
        root.setCenter(center);
        root.setBottom(actions);
    }

    private void buildBattleActions() {
        Button attack = new Button("Attacco");
        attack.setOnAction(e -> session.attack());
        abilityButton.setOnAction(e -> session.useAbility());

        itemChoice.setPromptText("Scegli un oggetto");
        itemChoice.setConverter(new StringConverter<>() {
            @Override
            public String toString(Consumable item) {
                return item == null ? "" : item.name();
            }

            @Override
            public Consumable fromString(String text) {
                throw new UnsupportedOperationException("La scelta dell'oggetto non è modificabile");
            }
        });
        useItemButton.disableProperty().bind(itemChoice.valueProperty().isNull());
        useItemButton.setOnAction(e -> session.useItem(itemChoice.getValue()));

        battleActions.setAlignment(Pos.CENTER);
        battleActions.getChildren().addAll(attack, abilityButton, itemChoice, useItemButton);
    }

    private void buildBetweenBattlesActions() {
        Button next = new Button("Prossimo combattimento");
        next.setDefaultButton(true);
        next.setOnAction(e -> session.startNextBattle());
        Button inventory = new Button("Inventario");
        inventory.setOnAction(e -> navigator.showInventory(session));
        Button save = new Button("Salva");
        save.setOnAction(e -> saveGame());
        Button menu = new Button("Menu principale");
        menu.setOnAction(e -> navigator.showMainMenu());

        betweenBattlesActions.setAlignment(Pos.CENTER);
        betweenBattlesActions.getChildren().addAll(next, inventory, save, menu);
    }

    private void buildGameOverActions() {
        Button menu = new Button("Torna al menu principale");
        menu.setOnAction(e -> navigator.showMainMenu());
        gameOverActions.setAlignment(Pos.CENTER);
        gameOverMessage.getStyleClass().add("subtitle");
        gameOverActions.getChildren().addAll(gameOverMessage, menu);
    }

    private void saveGame() {
        try {
            gameService.saveGame(session);
            addToLog("Partita salvata.");
        } catch (PersistenceException e) {
            Dialogs.showError("Impossibile salvare la partita: " + e.getMessage());
        }
    }

    private void refresh() {
        refreshPosition();
        refreshHero();
        refreshEnemy();
        refreshActions();
    }

    private void refreshPosition() {
        if (session.isOver()) {
            positionLabel.setText("Fine della partita");
        } else {
            positionLabel.setText("Piano %d di %d  ·  Combattimento %d".formatted(
                    session.progress().floor() + 1, session.floorCount(), session.progress().encounter() + 1));
        }
    }

    private void refreshHero() {
        Hero hero = session.hero();
        heroName.setText(hero.name());
        heroDetails.setText("%s  ·  Livello %d  ·  XP %d / %d".formatted(
                hero.heroClass().displayName(), hero.level(), hero.experience(), hero.experienceToNextLevel()));
        heroEquipment.setText("Attacco %d  ·  Difesa %d  ·  Arma: %s".formatted(
                hero.attackPower(), hero.defense(), hero.equippedWeapon().map(Item::name).orElse("nessuna")));
        heroHealth.show(hero);
    }

    private void refreshEnemy() {
        Optional<Enemy> enemy = session.currentEnemy();
        enemyName.setText(enemy.map(Enemy::name).orElse("Nessun nemico"));
        enemyHealth.setVisible(enemy.isPresent());
        enemy.ifPresent(enemyHealth::show);
    }

    private void refreshActions() {
        gameOverMessage.setText(session.hero().isAlive()
                ? "Hai completato il dungeon!" : "Il tuo eroe è caduto.");
        showOnly(session.isOver() ? gameOverActions
                : session.isInBattle() ? battleActions
                : betweenBattlesActions);

        int cooldown = session.abilityCooldownLeft();
        String abilityName = session.hero().ability().name();
        abilityButton.setText(cooldown == 0 ? abilityName : "%s (%d)".formatted(abilityName, cooldown));
        abilityButton.setDisable(cooldown > 0);

        itemChoice.getItems().setAll(session.hero().inventory().itemsOfType(Consumable.class));
        itemChoice.setValue(null);
    }

    private void showOnly(HBox visible) {
        for (HBox bar : new HBox[]{battleActions, betweenBattlesActions, gameOverActions}) {
            bar.setVisible(bar == visible);
            bar.setManaged(bar == visible);
        }
    }

    private void addToLog(String message) {
        log.getItems().add(message);
        log.scrollTo(log.getItems().size() - 1);
    }

    private static VBox panel(Node... children) {
        VBox panel = new VBox(6, children);
        panel.getStyleClass().add("panel");
        return panel;
    }
}
