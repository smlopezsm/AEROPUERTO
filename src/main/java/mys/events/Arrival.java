package mys.events;

import java.util.List;

import mys.entities.Entity;
import mys.generators.Constant;
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
            Distribution serviceDistribution, ServerSelectionPolicy serverSelectionPolicy) {
        this.clock = clock;
        this.entity = entity;
        this.serverSelectionPolicy = serverSelectionPolicy;
        this.arrivalDistribution = arrivalDistribution;
        this.serviceDistribution = serviceDistribution;
        this.wearDistribution = new Constant(0); //se asigna una distribucion de desgaste nula si no se proporciona
    }
    public Arrival(double clock, Entity entity, Distribution arrivalDistribution,
            Distribution serviceDistribution, Distribution wearDistribution,
            ServerSelectionPolicy serverSelectionPolicy) {
        this.clock = clock;
        this.entity = entity;
        this.serverSelectionPolicy = serverSelectionPolicy;
        this.arrivalDistribution = arrivalDistribution;
        this.serviceDistribution = serviceDistribution;
        this.wearDistribution = wearDistribution; //asignacion
    }


    //ver si se modifico para varios arribos a la vez
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
                            this.clock + this.serviceDistribution.sample(), //formula del descenso de los pasjeros, clock+tabla2+uniforme
                            this.entity(),
                            this.serviceDistribution,
                            this.wearDistribution)); //asegurarse d pasar la wearDistribution aca
        }

        //define como responde el trafico a las distribuciones exponenciales etapa 2
        //inyectamos el reloj actual si la distribucion es dependiente del tiempo
        if (this.arrivalDistribution instanceof TimeDependentExponential) {
            ((TimeDependentExponential) this.arrivalDistribution).setClock(this.clock);
        }

        double nextArrivalTime = this.clock + this.arrivalDistribution.sample();
        
        fel.insert(
                new Arrival(nextArrivalTime,
                        new Entity(this.entity().id() + 1, nextArrivalTime),
                        this.arrivalDistribution,
                        this.serviceDistribution,
                        this.wearDistribution, //se envia al prox arribo
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


