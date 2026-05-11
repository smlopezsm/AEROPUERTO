package mys.generators;

import java.util.Random;

/**
 * Distribución de Poisson con parámetro λ.
 * Genera enteros: número de eventos en un intervalo.
 * Algoritmo de Knuth (eficiente para λ pequeño).
 */
public class Poisson implements Distribution {

    private final double lambda;
    private final Random random;

    public Poisson(double lambda) {
        if (lambda <= 0) throw new IllegalArgumentException("λ debe ser positivo");
        this.lambda = lambda;
        this.random = new Random();
    }

    @Override
    public double sample() { //Aca se aplica la texnicas de caracterizacion? ver tecnicas de aceptacion y Rechazo
        // Algoritmo de Knuth: P(X=k) = e^(-λ) * λ^k / k! Estudiarlo mejor
        double L = Math.exp(-lambda);
        int k = 0;
        double p = 1.0;
        do {
            k++;
            p *= random.nextDouble();
        } while (p > L);
        return k - 1;
    }

    @Override
    public String toString() {
        return String.format("Poisson(λ=%.2f)", lambda);
    }
}