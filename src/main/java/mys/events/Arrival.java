package mys.events;

import java.util.List;

import mys.entities.Entity;
import mys.generators.Distribution;
import mys.resources.Server;
import mys.resources.ServerSelectionPolicy;


public class Arrival implements Event {

    private final double clock;
    private final int order = 100;
    private final Entity entity;
    private final ServerSelectionPolicy serverSelectionPolicy;
    private final Distribution arrivalDistribution;
    private final Distribution serviceDistribution;

    public Arrival(double clock, Entity entity, Distribution arrivalDistribution,
            Distribution serviceDistribution,
            ServerSelectionPolicy serverSelectionPolicy) {
        this.clock = clock;
        this.entity = entity;
        this.serverSelectionPolicy = serverSelectionPolicy;
        this.arrivalDistribution = arrivalDistribution;
        this.serviceDistribution = serviceDistribution;
    }

    @Override
    public void planificate(FutureEventList fel, List<Server> servers, Statistics statistics) {

        Server server = this.serverSelectionPolicy.selectServer(servers);

        if (server.isBusy()) {
            server.queue().add(this.entity());
            statistics.registerQueueLength(server.queue().size());
        } else {

            server.entity(this.entity());
            this.entity().server(server);

            statistics.addIdleTime(server.id(), this.clock);

            fel.insert(
                    new EndOfService(
                            this.clock + this.serviceDistribution.sample(),
                            this.entity(),
                            this.serviceDistribution));
        }

        double nextArrivalTime = this.clock + this.arrivalDistribution.sample();
        fel.insert(
                new Arrival(nextArrivalTime,
                        new Entity(this.entity().id() + 1, nextArrivalTime),
                        this.arrivalDistribution,
                        this.serviceDistribution,
                        this.serverSelectionPolicy));

        statistics.entityArrived();
    }

    @Override
    public double clock() {
        return this.clock;
    }

    @Override
    public int order() {
        return this.order;
    }

    @Override
    public Entity entity() {
        return this.entity;
    }

    @Override
    public Distribution distribution() {
        return this.arrivalDistribution;
    }
}