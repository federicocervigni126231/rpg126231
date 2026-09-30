package it.unicam.cs.mpgc.rpg126231.model.combat;

/**
 * Modificatori di un singolo colpo.
 *
 * @param multiplier     moltiplicatore della potenza d'attacco, positivo
 * @param ignoresDefense {@code true} se il colpo ignora la difesa del bersaglio
 */
public record HitModifier(double multiplier, boolean ignoresDefense) {

    /** Colpo normale, senza modificatori. */
    public static final HitModifier NORMAL = new HitModifier(1.0, false);

    /**
     * Crea il modificatore verificando che il moltiplicatore sia positivo.
     *
     * @throws IllegalArgumentException se il moltiplicatore non è positivo
     */
    public HitModifier {
        if (multiplier <= 0) {
            throw new IllegalArgumentException("Il moltiplicatore deve essere positivo");
        }
    }
}
