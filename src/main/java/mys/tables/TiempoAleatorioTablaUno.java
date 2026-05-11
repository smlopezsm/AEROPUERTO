package tables;

import java.util.Random;

public class TiempoAleatorioTablaUno implements DiscreteveProbability {
    private final Random random = new Random();

    @Override
    public double delta() {
        double prob = random.nextDouble();

        if (prob <= 0.35) {
            return 10;
        } else if (prob <= 0.35 + 0.45) {
            return 15;
        } else {
            return 17;
        }
    }
}
