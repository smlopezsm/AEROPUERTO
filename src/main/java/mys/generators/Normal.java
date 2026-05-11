package mys.generators;

import java.util.Random;

/**
 * Distribución Normal N(μ, σ).
 *
 * Usada para: desgaste de pista al aterrizar → N(μ=5, σ=1) unidades.
 * Se aplica un mínimo de 0 para evitar valores negativos (desgaste no puede ser negativo).
 */
public class Normal implements Distribution {

    private final double media;       // μ
    private final double desviacion;  // σ
    private final boolean soloPositivos;
    private final Random random;

    /**
     * @param media       μ: valor medio.
     * @param desviacion  σ: desviación estándar.
     * @param soloPositivos  si true, trunca en 0 (útil para desgaste).
     */
    public Normal(double media, double desviacion, boolean soloPositivos) {
        if (desviacion < 0) throw new IllegalArgumentException("σ debe ser >= 0");
        this.media = media;
        this.desviacion = desviacion;
        this.soloPositivos = soloPositivos;
        this.random = new Random();
    }

    public Normal(double media, double desviacion) {
        this(media, desviacion, false);
    }

    public Normal(double media, double desviacion, long semilla) {
        this(media, desviacion, false);
        this.random.setSeed(semilla);
    }

    @Override
    public double sample() {
        // Java usa el algoritmo Box-Muller internamente en nextGaussian()
        double valor = media + desviacion * random.nextGaussian(); //hay que estandarizar las varibles para la normal. z = media- mu /desviacion
        return soloPositivos ? Math.max(0, valor) : valor;
    } // ver que son las tecnicas de convulucion y ver si podemos aplicar esta tecnica para porder crear variables normales apartir de esta distribuciom.

    @Override
    public String toString() {
        return String.format("Normal(μ=%.2f, σ=%.2f)", media, desviacion);
    }
}