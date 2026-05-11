package mys;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mys.generators.Constant;
import mys.generators.EmpiricalDiscrete;

public class App {

    private static final double SIMULATION_LENGTH = 40320;
    private static final int NUMBER_OF_SERVERS = 1;

    public static void main(String[] args) {

        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_SERVERS; i++)
            servers.add(new Server(i, new LinkedList<>()));

        // Engine engine = new Engine(
        // SIMULATION_LENGTH,
        // servers,
        // new EmpiricalDiscrete(new double[] { 10d, 15d, 17d }, new double[] { .35d,
        // .45d, .2d }),
        // new EmpiricalDiscrete(new double[] { 8d, 10d, 13d, 15d },
        // new double[] { .38d, .32d, .1d, .2d }),
        // new OneServer(),
        // new Statistics());

        Engine engine = new Engine(
                SIMULATION_LENGTH,
                servers,
                new Constant(10),
                new Constant(9),
                new OneServer(),
                new Statistics());

        engine.run();

        new TerminalReport().report(engine.statistics());

    }
}
