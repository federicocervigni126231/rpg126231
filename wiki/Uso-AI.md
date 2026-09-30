# Uso di strumenti di AI

Questa pagina dichiara in dettaglio come e perché ho usato strumenti di intelligenza
artificiale nello sviluppo del progetto. Il registro completo, aggiornato fase per fase durante
il lavoro, è nel file [`AI_LOG.md`](https://github.com/federicocervigni126231/rpg126231/blob/main/AI_LOG.md) del repository.

## Strumento

- **Claude**, modello Claude Opus 5.5 di Anthropic, usato tramite **Claude Code** nell'app desktop
  di Claude.
- Claude Code lavora direttamente nella cartella del progetto: legge e scrive file, esegue
  comandi (`./gradlew build`, `git`) e ne legge l'output.
- Le istruzioni di lavoro date all'AI sono nel file [`CLAUDE.md`](https://github.com/federicocervigni126231/rpg126231/blob/main/CLAUDE.md) del
  repository: specifica ufficiale, requisiti architetturali, divisione in fasi e regole di
  collaborazione.

## Scopo

Ho usato l'AI come **tutor di progettazione object-oriented e come assistente alla scrittura del
codice**, con queste regole fissate in `CLAUDE.md`:

- lavorare **una fase alla volta** e fermarsi a fine fase per la mia approvazione;
- spiegare **il perché** di ogni scelta (principio SOLID, pattern, alternative scartate), così
  da poterla difendere all'orale;
- segnalare quando una mia idea peggiora il design o si allontana dalla specifica;
- eseguire `./gradlew build` dopo ogni modifica;
- **non fare commit al mio posto**, ma propormi il messaggio;
- tenere aggiornato `AI_LOG.md`.

## Uso per fase

| Fase | Cosa ho chiesto | Cosa ha prodotto l'AI | Il mio contributo |
|---|---|---|---|
| 1. Analisi | Definire la prima release e le funzionalità future | Elenco delle funzionalità, scelte di scope (per esempio abilità a ricarica invece del mana, un solo slot di salvataggio), esclusioni esplicite | Scelta del tipo di gioco e dei requisiti in `CLAUDE.md`; revisione e approvazione dello scope |
| 2. Design | Responsabilità, classi, diagramma, pattern | Architettura a livelli, diagramma delle classi, pattern con alternative scartate, piano dei test | Revisione e approvazione |
| 3. Setup | Repository e build | Configurazione Gradle, wrapper, `.gitignore`, README, finestra JavaFX minima | Scelta della cartella, creazione del repository GitHub pubblico e autenticazione, push |
| 4. Dominio | Classi del dominio con test | Package `model`, 50 test | Revisione, commit e push |
| 5. Logica | Combattimento, livelli, inventario | Package `service` ed eventi, 30 test | Revisione, commit e push |
| 6. Persistenza | Salvataggio JSON | Repository, conversione dei dati, contenuti di gioco, bilanciamento tramite simulazione | Revisione, commit e push |
| 7. GUI | Interfaccia JavaFX | Quattro schermate, navigazione, stile | Prova del gioco, commit e push |
| 8. Revisione | Conformità alla specifica e code review | Verifica da clone pulito, correzioni alla persistenza, aggiornamento di Gradle | Revisione, commit e push |
| 9. Wiki | Pagine della Wiki | Questa documentazione | Revisione e pubblicazione nella Wiki di GitHub |

Il codice sorgente, i test e la documentazione sono stati **scritti dall'AI**; io ho guidato il
lavoro con le istruzioni iniziali, ho rivisto e approvato ogni fase, ho provato il gioco ed
eseguito personalmente tutti i commit e i push. Nel registro `AI_LOG.md` la voce
"Mie modifiche: nessuna" indica che non ho modificato a mano il codice prodotto.

## Verifiche fatte sul lavoro dell'AI

Non ho accettato il codice sulla fiducia: ogni fase si chiudeva solo con la build verde e dopo
la mia approvazione. In particolare:

- **Test automatici.** 103 test JUnit 5 su dominio, logica e persistenza, eseguiti a ogni
  modifica.
- **Dipendenze tra livelli.** Controllate cercando gli import di ciascun package, per garantire
  che il dominio non dipenda da GUI e salvataggio.
- **Verifica visiva della GUI.** Un test temporaneo, poi rimosso, fotografava ogni schermata.
  Ha rivelato un bug reale: gli eventi della ricompensa venivano pubblicati con il combattimento
  ancora aperto, e la GUI mostrava i pulsanti di attacco a nemico sconfitto. Il bug è stato
  corretto e coperto da un test di regressione.
- **Clone pulito.** In revisione finale il repository è stato clonato da GitHub e compilato ed
  eseguito da zero, come farà chi valuta il progetto.

## Errori dell'AI emersi durante il lavoro

Per trasparenza, riporto i casi in cui l'AI ha sbagliato e come è stato gestito:

- **Affermazione non verificata sulla compatibilità di Gradle.** In revisione finale l'AI ha
  affermato che Gradle 8.14 non funzionasse con Java 25, basandosi sulla tabella di
  compatibilità ufficiale. Una prova pratica con JDK 25 l'ha smentita. L'aggiornamento a
  Gradle 9.8 è stato mantenuto come misura preventiva, perché è la versione ufficialmente
  supportata per Java 25-27.
- **Test scritti male.** In Fase 5 due test di `GameSession` non verificavano ciò che
  dichiaravano, per esempio usando un danno letale che chiudeva il combattimento prima del
  controllo. L'AI li ha individuati e riscritti prima di consegnare la fase.
- **Chiusura del gioco durante una mia prova.** Durante la revisione, un comando dell'AI per
  chiudere la propria istanza di prova ha chiuso anche la partita che stavo giocando.
  Il problema non riguarda il codice del progetto.
- **Bilanciamento iniziale.** Con i numeri proposti all'inizio il Mago vinceva solo il 4% delle
  partite. L'AI lo ha rilevato con una simulazione e ha corretto le statistiche prima di
  consegnare la fase.

## Considerazioni

L'AI ha accelerato molto la scrittura del codice e della documentazione e mi ha fatto da tutor
sulle scelte di design, spiegando pattern e principi applicati al progetto. Le decisioni di
scope e l'approvazione di ogni fase sono rimaste mie, e le verifiche descritte sopra hanno
permesso di intercettare gli errori che l'AI ha commesso.
