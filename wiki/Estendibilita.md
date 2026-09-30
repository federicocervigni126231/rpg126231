# Integrare nuove funzionalità

Il progetto è costruito perché le estensioni più probabili si aggiungano **con nuove classi o
nuovi dati**, senza modificare il codice esistente di dominio e logica (principio aperto/chiuso).
L'unico punto da toccare è di solito il composition root (`app`), che esiste proprio per
collegare i componenti.

## Punti di estensione della prima release

| Cosa aggiungere | Come | Cosa **non** va modificato |
|---|---|---|
| Nuova classe di eroe | Una classe che implementa `HeroClass` e una riga `register(...)` in `GameContent.heroClasses()`. Compare da sola nella schermata di creazione, che legge le classi dal registro. | `Hero`, `GameService`, GUI, salvataggio |
| Nuova abilità | Una classe che implementa `Ability`, restituita dalla classe di eroe. | `Battle`, `GameSession` |
| Nuovo comportamento dei nemici | Una classe che implementa `EnemyBehavior`. | `Enemy`, `Battle` |
| Nuovo nemico | Un nuovo `EnemyTemplate` in `GameContent.dungeon()`. Non servono nuove classi. | Tutto il resto |
| Nuovo oggetto dello stesso tipo | Un nuovo `Potion` o `Weapon` registrato in `GameContent.items()`. | Tutto il resto |
| Nuovo tipo di oggetto consumabile | Una classe che implementa `Consumable`, per esempio un antidoto, più la registrazione. Si usa in combattimento come le pozioni. | `Battle`, `Inventory`, salvataggio |
| Nuovi piani | Nuovi `Floor` in `GameContent.dungeon()`. | Tutto il resto |

## Funzionalità future previste

Queste funzionalità non sono implementate nella prima release. Ecco come si integrerebbero.

### Nuove classi di eroe e nuovi nemici

Sono già punti di estensione: vedi la tabella sopra. Per esempio, un Paladino è una classe
`Paladin implements HeroClass` con una nuova abilità `HolyStrike implements Ability`.

### Effetti di stato (veleno, stordimento)

1. Un'interfaccia `StatusEffect` in `model.combat`, con un metodo `onTurnStart(Combatant)` e la
   durata residua.
2. Una lista di effetti attivi in `Combatant`, con i metodi per aggiungerli e rimuoverli.
3. In `Battle`, una chiamata agli effetti all'inizio del turno di ciascun combattente.
4. Implementazioni come `Poison` (danno a ogni turno) e `Stun` (il turno viene saltato).
5. Abilità e comportamenti dei nemici applicano gli effetti; nuovi eventi (`StatusApplied`)
   informano la GUI.

È una modifica mirata in un solo punto di `Battle`; da lì in poi ogni nuovo effetto è solo una
nuova classe.

### Negozio

1. Un campo oro in `Hero`, guadagnato con le vittorie (`GameSession.rewardVictory`).
2. Un prezzo per gli oggetti, oppure un listino separato gestito dal negozio.
3. Un `ShopService` nel livello `service` che usa `Inventory` e il registro degli oggetti.
4. Una nuova schermata `ShopView` raggiungibile tra un combattimento e l'altro, e un nuovo metodo
   in `Navigator`.
5. Il campo oro in `SaveData` e in `SaveDataMapper`.

### Versione web o mobile

La logica non dipende da JavaFX, quindi una nuova interfaccia **non richiede modifiche a
dominio e logica**:

1. Si crea un nuovo package, per esempio `ui.web`, con un server HTTP o WebSocket.
2. Si ottengono `GameService` ed `EventBus` da `GameBootstrap`, come fa `RpgApplication`.
3. Le azioni del giocatore diventano chiamate a `GameService` e `GameSession`.
4. Un `GameEventListener` inoltra gli eventi al browser o all'app mobile.

Per più giocatori contemporanei basterebbe un `GameBootstrap` per sessione (ogni utente con il
suo `EventBus`) e un repository con un file o un record per utente.

### Salvataggio su database

1. Una classe `DatabaseGameRepository implements GameRepository` nel package `persistence`, per
   esempio con JDBC.
2. Può riusare `SaveDataMapper`, perché `SaveData` è già una rappresentazione piatta e adatta a
   una tabella.
3. In `GameBootstrap` si passa la nuova implementazione al posto di `JsonGameRepository`.

`GameService`, `GameSession` e la GUI non cambiano, perché conoscono solo l'interfaccia.

## Riepilogo dei meccanismi

| Meccanismo | Cosa rende possibile |
|---|---|
| Interfacce Strategy (`Ability`, `EnemyBehavior`, `HeroClass`, `Consumable`) | Nuovi comportamenti come nuove classi |
| `Registry` e identificativi | Nuovi elementi disponibili alla GUI e salvabili senza modifiche |
| `EnemyTemplate` e `GameContent` | Nuovi nemici e piani come semplici dati |
| `EventBus` (Observer) | Nuove interfacce che ascoltano il gioco senza modificarlo |
| `GameSession` (Facade) | Un solo punto d'accesso stabile per ogni interfaccia |
| `GameRepository` (Repository) | Nuovi supporti di salvataggio |
| `GameBootstrap` (composition root) | Sostituire un componente in un solo punto |
