package mys.events;

import java.util.List;

import mys.entities.Entity;
import mys.generators.Distribution;
import mys.generators.TimeDependentExponential;
import mys.resources.Server;
import mys.resources.ServerSelectionPolicy;

public class Arrival implements Event {

    private final double clock;
    private final int order = 100;
    private final Entity entity;
    private final ServerSelectionPolicy serverSelectionPolicy;
    private final Distribution arrivalDistribution;
    private final Distribution serviceDistribution;
    private final Distribution wearDistribution; 

    public Arrival(double clock, Entity entity, Distribution arrivalDistribution,
            Distribution serviceDistribution, Distribution wearDistribution,
            ServerSelectionPolicy serverSelectionPolicy) {
        this.clock = clock;
        this.entity = entity;
        this.serverSelectionPolicy = serverSelectionPolicy;
        this.arrivalDistribution = arrivalDistribution;
        this.serviceDistribution = serviceDistribution;
        this.wearDistribution = wearDistribution; // ASIGNACIÓN
    }

    // Ver si se modifico para varios arribos a la vez
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
                            this.clock + this.serviceDistribution.sample(), //Formula del descenso de los pasjeros, clock+tabla2+uniforme
                            this.entity(),
                            this.serviceDistribution,
                            this.wearDistribution)); // Asegurate de pasar la wearDistribution acá
        }

        // --- CÓDIGO NUEVO AGREGADO --- 
        // Define como responde el trafico a las distribuciones exponenciales Etapa 2
        // Inyectamos el reloj actual si la distribución es dependiente del tiempo
        if (this.arrivalDistribution instanceof TimeDependentExponential) {
            ((TimeDependentExponential) this.arrivalDistribution).setClock(this.clock);
        }
        // ------------------------------

        double nextArrivalTime = this.clock + this.arrivalDistribution.sample();
        
        fel.insert(
                new Arrival(nextArrivalTime,
                        new Entity(this.entity().id() + 1, nextArrivalTime),
                        this.arrivalDistribution,
                        this.serviceDistribution,
                        this.wearDistribution, // SE ENVÍA AL PRÓXIMO ARRIBO
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


