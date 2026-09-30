# Registro d'uso dell'AI

Strumento: Claude (Anthropic), usato tramite Claude Code nell'app desktop.
Per ogni fase: cosa ho chiesto, cosa ha prodotto l'AI, cosa ho modificato io.

## Fase 1: Analisi

- **Richiesta:** partire dalla specifica ufficiale e dall'idea del gioco (dungeon crawler
  a turni) per definire le funzionalità della prima release e quelle future.
- **Prodotto dall'AI:** tabella delle funzionalità della prima release (3 classi con
  abilità a ricarica, 5 nemici con 2 comportamenti di IA, 5 piani con boss finale,
  inventario con pozioni e armi, livelli, salvataggio JSON a slot unico, 4 schermate)
  e delle funzionalità future, con le esclusioni esplicite (fuga, mana, oro, armature,
  più slot, mappa esplorabile, audio).
- **Mie modifiche:** nessuna, ho approvato la proposta.

## Fase 2: Design

- **Richiesta:** responsabilità, classi e interfacce, diagramma delle classi in Mermaid,
  pattern e motivazioni.
- **Prodotto dall'AI:** architettura a livelli (model, service, persistence, ui.javafx,
  app), diagramma delle classi, tabella delle responsabilità, scelta dei pattern
  (Strategy per abilità e IA, composizione per le classi di eroe, Factory + Registry,
  Observer, Facade, Repository con oggetti di trasferimento dati, iniezione delle
  dipendenze manuale) con le alternative scartate, mappatura SOLID, punti di estensione
  e piano dei test.
- **Mie modifiche:** nessuna, ho approvato il design.

## Fase 3: Setup

- **Richiesta:** creare il repository in `~/IdeaProjects/rpg126231` e predisporre la build.
- **Prodotto dall'AI:** `settings.gradle` (con foojay resolver per scaricare il JDK 21),
  `build.gradle` (toolchain Java 21, plugin JavaFX, Gson, JUnit 5), wrapper Gradle,
  `.gitignore`, `.gitattributes`, README minimale, classe `RpgApplication` minima per
  verificare `./gradlew run`.
- **Mie modifiche:** nessuna. Ho chiesto all'AI di eseguire anche il primo commit e la
  pubblicazione su GitHub.

## Fase 4: Dominio e test

- **Richiesta:** implementare il dominio progettato in Fase 2, con test JUnit 5.
- **Prodotto dall'AI:** package `model` con `Identifiable` e `Registry`; `model.combat`
  (`Stats`, `Combatant`, `HitModifier`, `DamageResolver`); `model.item` (`Item`,
  `Consumable`, `Potion`, `Weapon`, `Inventory`); `model.ability` (`Ability`,
  `PowerStrike`, `Fireball`, `DoubleStrike`); `model.hero` (`HeroClass`, `Warrior`,
  `Mage`, `Rogue`, `Hero`); `model.enemy` (`Enemy`, `EnemyBehavior`,
  `AggressiveBehavior`, `BerserkerBehavior`, `EnemyTemplate`); `model.dungeon`
  (`Dungeon`, `Floor`, `DungeonProgress`). 50 test JUnit 5, con un `DamageResolver`
  finto (`RecordingDamageResolver`) per rendere prevedibili i test di abilità e nemici.
  Rispetto al design: aggiunti `Identifiable` (id comune per il `Registry`) e il record
  `Floor` (piano del dungeon).
- **Mie modifiche:** _da compilare_

## Fase 5: Logica di gioco

- **Richiesta:** implementare combattimento, livelli e inventario nel livello service, con test.
- **Prodotto dall'AI:** package `service.event` (`GameEvent` sealed con gli eventi come
  record annidati, `GameEventListener`, `EventBus`: pattern Observer); package `service`
  con `DamageCalculator` (formula del danno, `RandomGenerator` iniettato), `LootGenerator`
  (oggetti lasciati dai nemici), `Battle` (turni, ricarica dell'abilità, esito; visibile solo
  nel package) e `GameSession` (Facade per la GUI: avvio dei combattimenti, esperienza,
  bottino, avanzamento, vittoria e sconfitta). Aggiunti `BattleOutcome` in `model.combat`
  e `Dungeon.contains`. 30 nuovi test (80 in totale).
- **Mie modifiche:** _da compilare_
