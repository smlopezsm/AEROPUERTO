package tables;

import java.util.Random;
public class TiempoAleatorioTablaDos implements DiscreteveProbability{
    private final Random random = new Random();

    @Override
    public double delta(){
        double prob = random.nextDouble();

        if(prob <= 0.38) {
            return 8;
        } else if (prob <= 0.38 + 0.32) {
            return 10;
        }else if (prob <= (0.38 + 0.32 + 0.1)) {
            return 13;
        }else {
            return 15;
        }
    }
}
