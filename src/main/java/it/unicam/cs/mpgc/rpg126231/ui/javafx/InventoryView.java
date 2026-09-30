package it.unicam.cs.mpgc.rpg126231.ui.javafx;

import it.unicam.cs.mpgc.rpg126231.model.hero.Hero;
import it.unicam.cs.mpgc.rpg126231.model.item.Item;
import it.unicam.cs.mpgc.rpg126231.model.item.Potion;
import it.unicam.cs.mpgc.rpg126231.model.item.Weapon;
import it.unicam.cs.mpgc.rpg126231.service.GameSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Inventario dell'eroe: elenco degli oggetti ed equipaggiamento delle armi.
 * Le pozioni si usano durante il combattimento.
 */
class InventoryView implements View {

    private final Navigator navigator;
    private final GameSession session;
    private final VBox root = new VBox(12);
    private final Label summary = new Label();
    private final ListView<Item> items = new ListView<>();

    /**
     * Crea la schermata.
     *
     * @param navigator navigatore tra le schermate
     * @param session   partita in corso
     */
    InventoryView(Navigator navigator, GameSession session) {
        this.navigator = navigator;
        this.session = session;
        build();
        refresh();
    }

    @Override
    public Parent root() {
        return root;
    }

    private void build() {
        Label title = new Label("Inventario");
        title.getStyleClass().add("title");

        items.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : describe(item));
            }
        });
        items.setPlaceholder(new Label("L'inventario è vuoto."));
        VBox.setVgrow(items, Priority.ALWAYS);

        Button equip = new Button("Equipaggia");
        equip.disableProperty().bind(items.getSelectionModel().selectedItemProperty().map(
                item -> !(item instanceof Weapon)).orElse(true));
        equip.setOnAction(e -> equipSelected());

        Button back = new Button("Indietro");
        back.setCancelButton(true);
        back.setOnAction(e -> navigator.showBattle(session));

        HBox buttons = new HBox(12, back, equip);
        buttons.setAlignment(Pos.CENTER);

        root.setPadding(new Insets(20));
        root.getChildren().addAll(title, summary, new Label("Le pozioni si usano durante il combattimento."),
                items, buttons);
    }

    private void equipSelected() {
        if (items.getSelectionModel().getSelectedItem() instanceof Weapon weapon) {
            session.equip(weapon);
            refresh();
        }
    }

    private void refresh() {
        Hero hero = session.hero();
        summary.setText("%s  ·  Attacco %d  ·  Arma equipaggiata: %s".formatted(
                hero.name(), hero.attackPower(),
                hero.equippedWeapon().map(InventoryView::describe).orElse("nessuna")));
        items.getItems().setAll(hero.inventory().items());
    }

    private static String describe(Item item) {
        return switch (item) {
            case Potion potion -> "%s (cura %d PV)".formatted(potion.name(), potion.healAmount());
            case Weapon weapon -> "%s (+%d attacco)".formatted(weapon.name(), weapon.attackBonus());
            default -> item.name();
        };
    }
}
