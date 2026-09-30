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
