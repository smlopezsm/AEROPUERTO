package mys;

import mys.generators.Distribution;

import java.util.List;
import mys.events.FutureEventList;
import mys.events.Statistics;
import mys.resources.Server;
import mys.resources.ServerSelectionPolicy;
import mys.entities.Entity;
import mys.events.Arrival;
import mys.events.Event;
public class Engine {

    private final double simulationLenght;
    private final List<Server> servers;
    private final FutureEventList fel;
    private final Statistics statistics;

    public Engine(
            double simulationLenght,
            List<Server> servers,
            Distribution arrivalBehavior,
            Distribution serviceBehavior,
            ServerSelectionPolicy serverSelectionPolicy,
            Statistics statistics) {

        this.simulationLenght = simulationLenght;
        this.servers = servers;
        this.statistics = statistics;

        this.fel = new FutureEventList();

        this.fel.insert(
                new Arrival(0, new Entity(0, 0), arrivalBehavior, serviceBehavior, serverSelectionPolicy));
    }

    public Statistics statistics() {
        return this.statistics;
    }

    public void run() {

        Event e = this.fel.imminent();
        while (e.clock() <= simulationLenght) {
            e.planificate(this.fel, this.servers, this.statistics);
            e = this.fel.imminent();
        }

        this.statistics.calculate();
    }
}
