package mys.generators;

import java.util.Random;

/**
 * Distribución Empírica Discreta.
 * Dada una tabla de valores y sus probabilidades asociadas,
 * genera muestras mediante el método de la transformada inversa discreta.
 *
 * Ejemplo de uso:
 *   double[] valores = {1.0, 2.0, 3.0};
 *   double[] probs   = {0.2, 0.5, 0.3};
 *   IGeneradorAleatorio gen = new EmpiricalDiscrete(valores, probs);
 */
public class EmpiricalDiscrete implements Distribution {

    private final double[] valores;
    private final double[] probAcumulada;  // tabla CDF
    private final Random random;

    public EmpiricalDiscrete(double[] valores, double[] probabilidades) {
        if (valores.length != probabilidades.length)
            throw new IllegalArgumentException("Los arrays deben tener el mismo tamaño");

        this.valores = valores.clone();
        this.probAcumulada = construirCDF(probabilidades);
        this.random = new Random();
    }

    /**
     * Construye la Función de Distribución Acumulada (CDF) a partir de probabilidades discretas.
     *
     * QUE HACE:
     *   Convierte un array de probabilidades individuales P(X=x_i) en un array de probabilidades
     *   acumuladas, donde cada posición i contiene la suma de todas las probabilidades desde el
     *   inicio hasta ese punto: CDF[i] = P(X <= x_i).
     *   Esta CDF es esencial para el método de la transformada inversa discreta.
     *
     * COMO LO HACE:
     *   1. Recorre el array de probabilidades y calcula la suma acumulativa en cada posición.
     *   2. Normaliza el último valor a 1.0 para corregir errores de punto flotante.
     *   3. Valida que las probabilidades sumen aproximadamente 1.0.
     *
     * EJEMPLOS DE UTILIZACIÓN:
     *   Dado: probs = [0.2, 0.5, 0.3]
     *   Resultado: cdf  = [0.2, 0.7, 1.0]
     *
     *   Uso típico en generar():
     *     - Generar u = random en [0, 1)
     *     - Buscar el primer índice i donde u <= cdf[i]
     *     - Retornar valores[i]
     *
     *   Ejemplo práctico - Simular lanzamiento de dado sesgado:
     *     valores = {1, 2, 3, 4, 5, 6}
     *     probs   = {0.1, 0.1, 0.2, 0.2, 0.2, 0.2}
     *     cdf     = {0.1, 0.2, 0.4, 0.6, 0.8, 1.0}
     *     Si u = 0.35, como 0.35 > 0.2 y 0.35 <= 0.4, retorna valores[2] = 3
     *
     * @param probs Array de probabilidades individuales que deben sumar 1
     * @return Array con las probabilidades acumuladas (CDF)
     */
    private double[] construirCDF(double[] probs) {
        double[] cdf = new double[probs.length];
        double suma = 0;
        for (int i = 0; i < probs.length; i++) {
            suma += probs[i];
            cdf[i] = suma;
        }
        cdf[cdf.length - 1] = 1.0;

        double total = 0;
        for (double p : probs) total += p;
        if (Math.abs(total - 1.0) > 0.001)
            throw new IllegalArgumentException("Las probabilidades deben sumar 1 (suma actual: " + total + ")");

        return cdf;
    }

    @Override
    public double sample() {
        double u = random.nextDouble();
        for (int i = 0; i < probAcumulada.length; i++) {
            if (u <= probAcumulada[i]) return valores[i];
        }
        return valores[valores.length - 1]; // por seguridad
    }

    @Override
    public String toString() {
        return "EmpiricaDiscreta(" + valores.length + " valores)";
    }
}