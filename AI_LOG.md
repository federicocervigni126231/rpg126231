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
- **Mie modifiche:** nessuna.

## Fase 5: Logica di gioco

- **Richiesta:** implementare combattimento, livelli e inventario nel livello service, con test.
- **Prodotto dall'AI:** package `service.event` (`GameEvent` sealed con gli eventi come
  record annidati, `GameEventListener`, `EventBus`: pattern Observer); package `service`
  con `DamageCalculator` (formula del danno, `RandomGenerator` iniettato), `LootGenerator`
  (oggetti lasciati dai nemici), `Battle` (turni, ricarica dell'abilità, esito; visibile solo
  nel package) e `GameSession` (Facade per la GUI: avvio dei combattimenti, esperienza,
  bottino, avanzamento, vittoria e sconfitta). Aggiunti `BattleOutcome` in `model.combat`
  e `Dungeon.contains`. 30 nuovi test (80 in totale).
- **Mie modifiche:** nessuna.

## Fase 6: Persistenza JSON

- **Richiesta:** salvataggio e caricamento su file JSON dietro un'interfaccia repository.
- **Prodotto dall'AI:** nel livello service `GameRepository` (interfaccia), `SavedGame`,
  `PersistenceException` e `GameService` (nuova partita, carica, salva solo tra i
  combattimenti); package `persistence` con `SaveData` (oggetti di trasferimento dati con
  solo tipi semplici e identificativi), `SaveDataMapper` (conversione dominio ↔ dati con i
  registri) e `JsonGameRepository` (Gson, scrittura su file temporaneo e sostituzione
  atomica, errori tradotti in `PersistenceException`); package `app` con `GameContent`
  (classi, oggetti, 5 nemici, 5 piani) e `GameBootstrap` (composition root). Bilanciamento
  dei numeri tramite una simulazione Monte Carlo usa e getta (non inclusa nel progetto):
  modificate le statistiche di Mago e Ladro. 20 nuovi test (100 in totale).
- **Mie modifiche:** nessuna.

## Fase 7: Interfaccia grafica JavaFX

- **Richiesta:** GUI JavaFX con menu, creazione del personaggio, combattimento e inventario.
- **Prodotto dall'AI:** package `ui.javafx` con `RpgApplication` (avvio tramite
  `GameBootstrap`), `Navigator` (crea le schermate e le alterna in un'unica scena), `View`,
  `MainMenuView`, `CharacterCreationView` (classi lette dal registro), `BattleView`
  (ascoltatore degli eventi, pattern Observer), `InventoryView`, più `EventFormatter`
  (eventi → messaggi), `HealthBar` e `Dialogs`; foglio di stile `style.css`. Aggiunto
  `GameSession.equip` perché la GUI modifichi lo stato solo tramite il Facade.
  Verifica visiva con un test temporaneo che fotografa ogni schermata (poi rimosso): ha
  rivelato un bug in `GameSession` (gli eventi della ricompensa venivano pubblicati con il
  combattimento ancora aperto), corretto e coperto da un test di regressione, e alcuni
  difetti di layout. 102 test in totale.
- **Mie modifiche:** nessuna.

## Fase 8: Revisione finale

- **Richiesta:** controllo punto per punto della conformità alla specifica e code review
  severa sui criteri di valutazione.
- **Prodotto dall'AI:** verifica di package, visibilità del repository, clone pulito da
  GitHub con `./gradlew build` e `./gradlew run`, assenza di codice morto e direzione delle
  dipendenze tra livelli. Correzioni: `SaveDataMapper` segnala i dati mancanti con un
  errore esplicito invece di affidarsi a `NullPointerException` (con test);
  `JsonGameRepository` ripiega sullo spostamento non atomico dove non è supportato e
  cancella il file temporaneo in caso di errore; `Hero.EXPERIENCE_PER_LEVEL` resa privata.
  Wrapper aggiornato da Gradle 8.14.3 a 9.8.0 con verifica del checksum della
  distribuzione, per avere il supporto ufficiale di Java 25-27; verificato build e avvio
  anche con JDK 25. Nota: l'AI aveva inizialmente affermato che Gradle 8.14 non
  funzionasse con Java 25; la prova pratica ha smentito l'affermazione, e l'aggiornamento
  è stato mantenuto come misura preventiva.
- **Mie modifiche:** nessuna.

## Fase 9: Wiki

- **Richiesta:** scrivere le pagine della Wiki GitHub in `wiki/`, una per ogni punto della
  specifica, più la dichiarazione dettagliata sull'uso dell'AI.
- **Prodotto dall'AI:** `Home`, `_Sidebar` (navigazione), `Funzionalita`, `Responsabilita`
  (con diagramma delle dipendenze verificato sugli import reali), `Classi-e-interfacce`
  (tre diagrammi Mermaid, tabella di ogni tipo, pattern e alternative scartate),
  `Persistenza`, `Estendibilita` (punti di estensione e integrazione delle funzionalità
  future), `Uso-AI` (dichiarazione basata su questo registro, con gli errori dell'AI emersi).
- **Mie modifiche:** nessuna.
