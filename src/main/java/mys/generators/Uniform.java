package mys.generators;

import java.util.Random;

public class Uniform implements Distribution {

    private final double min;
    private final double max;
    private final Random random;

    public Uniform(double min, double max) {
        if (min >= max) throw new IllegalArgumentException("min debe ser menor que max");
        this.min = min;
        this.max = max;
        this.random = new Random();
    }

    /**constructor con semilla (es util para las pruebas reproducibles). */
    public Uniform(double min, double max, long semilla) {
        this(min, max);
        this.random.setSeed(semilla);
    }

    @Override
    public double sample() {
        //U(a,b) = a + (b - a) * U(0,1)
        return min + (max - min) * random.nextDouble(); //x = a + (b-a) * r
    }

    @Override
    public String toString() {
        return String.format("Uniforme(%.2f, %.2f)", min, max);
    }
}