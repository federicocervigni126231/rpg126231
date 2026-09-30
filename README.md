# Dungeon RPG

Gioco di ruolo a turni sviluppato in Java 21 e JavaFX per il progetto d'esame di
Metodologie di Programmazione (UNICAM, A.A. 2025/26).

L'eroe sceglie una classe (Guerriero, Mago, Ladro), affronta i nemici piano per piano
in combattimenti a turni, raccoglie pozioni e armi, sale di livello e può salvare e
caricare la partita su file JSON.

La documentazione del progetto (funzionalità, responsabilità, classi, persistenza,
estendibilità) si trova nella Wiki del repository.

## Requisiti

- JDK 17 o superiore per avviare Gradle. Il JDK 21 usato per compilare viene scaricato
  automaticamente dalla toolchain di Gradle, se non è già installato.

## Esecuzione

```bash
./gradlew build
./gradlew run
```

Su Windows: `gradlew.bat build` e `gradlew.bat run`.

## Uso di strumenti di AI

Durante lo sviluppo è stato usato Claude (Anthropic), tramite Claude Code, come tutor
per l'analisi, la progettazione, la scrittura del codice e della documentazione.
Ogni scelta è stata verificata e rivista dallo studente. Il dettaglio di come e perché
è stato usato si trova nella pagina della Wiki dedicata all'AI e nel file
[AI_LOG.md](AI_LOG.md).
