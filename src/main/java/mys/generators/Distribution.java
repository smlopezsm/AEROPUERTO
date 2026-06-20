package mys.generators;

@FunctionalInterface
public interface Distribution {
    double sample();
}

//falta decirle al manager que distribuon va a utilizar 
