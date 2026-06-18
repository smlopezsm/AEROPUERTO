package mys.tables;

import mys.generators.Distribution;

@FunctionalInterface
public interface DiscreteveProbability extends Distribution {
    double delta();
    @Override
    default double sample() {
        return delta();
    }
}
