# Funzionalità implementate

## Flusso di gioco

1. **Menu principale:** nuova partita, caricamento della partita salvata (attivo solo se esiste
   un salvataggio), uscita.
2. **Creazione del personaggio:** nome dell'eroe e scelta della classe, con statistiche e abilità
   di ciascuna.
3. **Dungeon:** 5 piani da 2 combattimenti ciascuno. Tra un combattimento e l'altro il giocatore
   può proseguire, aprire l'inventario, salvare o tornare al menu.
4. **Fine della partita:** vittoria dopo aver sconfitto il Drago all'ultimo piano, sconfitta se
   l'eroe perde tutti i punti vita.

## Classi di eroe

| Classe | PV | Attacco | Difesa | Crescita per livello | Abilità speciale |
|---|---|---|---|---|---|
| Guerriero | 120 | 14 | 8 | +15 PV, +3 ATT, +2 DIF | **Colpo Potente:** un colpo con attacco doppio (ricarica 3 turni) |
| Mago | 90 | 19 | 6 | +10 PV, +4 ATT, +2 DIF | **Palla di Fuoco:** colpo ×1,5 che ignora la difesa (ricarica 3 turni) |
| Ladro | 100 | 16 | 6 | +12 PV, +3 ATT, +2 DIF | **Doppio Colpo:** due colpi consecutivi (ricarica 2 turni) |

## Nemici

| Nemico | PV | Attacco | Difesa | Esperienza | Comportamento | Può lasciare |
|---|---|---|---|---|---|---|
| Goblin | 40 | 10 | 2 | 30 | Aggressivo | Pozione piccola, Pugnale |
| Lupo | 55 | 13 | 3 | 45 | Aggressivo | Pozione piccola |
| Scheletro | 70 | 15 | 6 | 60 | Aggressivo | Pozione piccola, Spada |
| Orco | 95 | 17 | 6 | 90 | Berserker | Pozione grande, Bastone runico |
| Drago | 170 | 22 | 7 | 200 | Berserker | — |

- **Aggressivo:** attacca sempre con un colpo normale.
- **Berserker:** quando i suoi punti vita scendono al 30% o meno, colpisce con forza ×1,5.

Ordine dei combattimenti: Goblin e Lupo (piano 1), Lupo e Scheletro (2), Scheletro e Orco (3),
Orco e Scheletro (4), Orco e Drago (5).

## Combattimento a turni

A ogni turno l'eroe sceglie un'azione, poi il nemico risponde se è ancora vivo.

- **Attacco:** colpo normale.
- **Abilità speciale:** effetto della classe; dopo l'uso resta in ricarica per un certo numero di
  turni, mostrato sul pulsante.
- **Usa oggetto:** una pozione dall'inventario, che viene consumata.

Danno di un colpo: `max(1, attacco × moltiplicatore − difesa) + casuale(0..2)`. L'attacco
dell'eroe include il bonus dell'arma equipaggiata.

## Esperienza e livelli

Ogni nemico sconfitto dà esperienza. Per passare dal livello N al successivo servono N × 100
punti; l'esperienza in eccesso si conserva e un grande guadagno può far salire più livelli.
Salendo di livello le statistiche crescono secondo la classe e i punti vita tornano al massimo.
I punti vita non si ricaricano tra un combattimento e l'altro.

## Oggetti e inventario

| Oggetto | Effetto |
|---|---|
| Pozione piccola | Cura 30 PV |
| Pozione grande | Cura 60 PV |
| Pugnale | +4 attacco |
| Spada | +5 attacco |
| Bastone runico | +6 attacco |

Un nemico sconfitto lascia un oggetto con probabilità del 50%. Le pozioni si usano in
combattimento; le armi si equipaggiano dalla schermata dell'inventario, tra un combattimento e
l'altro. Equipaggiando una nuova arma, quella precedente torna nell'inventario.

## Salvataggio e caricamento

La partita si salva tra un combattimento e l'altro in `~/.rpg126231/save.json` (cartella
dell'utente, valida su Windows, macOS e Linux) e si riprende dal menu principale. Uno slot unico:
ogni salvataggio sostituisce il precedente. I dettagli sono nella pagina
[Dati e persistenza](Persistenza).

## Bilanciamento

I numeri sono stati tarati simulando 3000 partite per classe con una strategia semplice
(abilità appena pronta, pozione sotto il 35% dei PV). Percentuali di vittoria risultanti:
Guerriero 97%, Ladro 88%, Mago 63%. Il Mago è la classe più difficile, coerentemente con il suo
ruolo di attaccante fragile.

## Escluso dalla prima release

Per scelta, e per tenere lo scope ridotto: fuga dal combattimento, mana, oro, armature, più slot
di salvataggio, mappa esplorabile, audio. Le funzionalità future previste sono descritte in
[Integrare nuove funzionalità](Estendibilita).
