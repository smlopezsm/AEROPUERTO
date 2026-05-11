package mys.generators;

import java.util.Random;

public class Exponential implements Distribution{

    private final double media;
    private final Random random;

    public Exponential(double media){
        if (media <= 0) throw new IllegalArgumentException("la media debe ser positiva");
        this.media=media;
        this.random= new Random();
    }

    public Exponential(double media, long semilla){
        this(media);
        this.random.setSeed(semilla);
    }

    @Override
    public double sample(){
        double u;
        do{ //ln(0) = infinito lo que rompe el codigo, por lo que usamos el do while como proteccion a este caso.
            u=random.nextDouble();
        }while ( u == 0.0);
        return -media * Math.log(u); //(-1/labda)*(ln (1-r)) formula de clase al sacar la inversa a la formula de la expornencial.
    }

    public double getMedia(){
        return  media;
    }

    @Override
    public String toString(){
        return String.format("Exponencial (u=%.2f)", media);
    }

}
