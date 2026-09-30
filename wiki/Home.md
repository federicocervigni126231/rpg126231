# Dungeon RPG

Gioco di ruolo a turni sviluppato in Java 21 e JavaFX per il progetto d'esame di
**Metodologie di Programmazione** (UNICAM, A.A. 2025/26). Questa Wiki sostituisce la relazione.

L'eroe sceglie una classe (Guerriero, Mago o Ladro) e attraversa un dungeon di cinque piani,
affrontando un nemico dopo l'altro in combattimenti a turni. Sconfiggendo i nemici guadagna
esperienza e oggetti; alla fine del quinto piano lo attende il Drago. La partita si può salvare
tra un combattimento e l'altro e riprendere in seguito.

## Pagine

| Pagina | Contenuto |
|---|---|
| [Funzionalità implementate](Funzionalita) | Cosa si può fare nella prima release |
| [Responsabilità individuate](Responsabilita) | Come è suddiviso il sistema e perché |
| [Classi e interfacce](Classi-e-interfacce) | Ogni tipo del progetto con la sua responsabilità, diagrammi delle classi, pattern |
| [Dati e persistenza](Persistenza) | Organizzazione dei dati e salvataggio su file JSON |
| [Integrare nuove funzionalità](Estendibilita) | Punti di estensione e come aggiungere le funzionalità future |
| [Uso di strumenti di AI](Uso-AI) | Dichiarazione dettagliata dell'uso dell'AI e del suo scopo |

## Avvio

Serve un JDK 17 o superiore per avviare Gradle; il JDK 21 usato per compilare viene scaricato
automaticamente se manca.

```bash
./gradlew build
./gradlew run
```

## In breve

- **Architettura a livelli:** dominio (`model`), logica di gioco (`service`), persistenza
  (`persistence`), interfaccia grafica (`ui.javafx`), più un punto di assemblaggio (`app`).
- **Pattern principali:** Strategy, Observer, Facade, Repository, Factory/Registry,
  composition root con iniezione delle dipendenze manuale.
- **Qualità:** 103 test JUnit 5 su dominio, logica e persistenza; build verificata da un clone
  pulito; compilazione con la toolchain Java 21, Gradle avviato con JDK 23 e JDK 25.
