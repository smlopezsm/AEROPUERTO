package mys.generators;

public class Constant implements Distribution {

    private final double value;

    public Constant(double value) {
        this.value = value;
    }

    @Override
    public double sample() {
        return this.value;
    }

}
