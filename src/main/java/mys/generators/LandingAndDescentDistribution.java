package mys.generators;

public class LandingAndDescentDistribution implements Distribution {
    
    private final Distribution landingDistribution;
    private final Distribution descentDistribution;

    public LandingAndDescentDistribution(Distribution landingDistribution, Distribution descentDistribution) {
        this.landingDistribution = landingDistribution;
        this.descentDistribution = descentDistribution;
    }

    @Override
    public double sample() {
        return this.landingDistribution.sample() + this.descentDistribution.sample();
    }
}