package mys.events;

import java.util.List;

import mys.entities.Entity;
import mys.generators.Distribution;
import mys.resources.Server;


public class EndOfService implements Event {

    private final double clock;
    private final int order = 200;
    private final Entity entity;
    private final Distribution distribution;

    public EndOfService(double clock, Entity entity, Distribution distribution) {
        this.clock = clock;
        this.entity = entity;
        this.distribution = distribution;
    }

    @Override
    public void planificate(FutureEventList fel, List<Server> servers, Statistics statistics) {

        Server server = this.entity.server();

        if (server.queue().size() > 0) {

            Entity nextEntity = server.queue().poll();
            server.entity(nextEntity);
            nextEntity.server(server);

            double serviceTime = this.distribution.sample();

            fel.insert(new EndOfService(this.clock + serviceTime, nextEntity, this.distribution));

            statistics.addWaitingTime(this.clock - nextEntity.arrivalTime());

        } else {
            server.entity(null);
            statistics.initIdleTime(server.id(), this.clock);
        }

        statistics.entityDeparture();
        statistics.addSystemTime(this.clock - this.entity.arrivalTime());

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
        return this.distribution;
    }
}
