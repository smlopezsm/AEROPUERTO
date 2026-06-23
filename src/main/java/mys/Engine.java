package mys;

import java.util.List;

import mys.entities.Entity;
import mys.events.Arrival;
import mys.events.Event;
import mys.events.FutureEventList;
import mys.events.Statistics;
import mys.generators.Distribution;
import mys.resources.Server;
import mys.resources.ServerSelectionPolicy;
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
        this.statistics.registerServers(servers);
        this.fel = new FutureEventList();

        this.fel.insert(
                new Arrival(0, new Entity(0, 0), arrivalBehavior, serviceBehavior, serverSelectionPolicy));
    }

    public Engine(
            double simulationLenght,
            List<Server> servers,
            Distribution arrivalBehavior,
            Distribution serviceBehavior,
            Distribution wearBehavior, 
            ServerSelectionPolicy serverSelectionPolicy,
            Statistics statistics) {

        this.simulationLenght = simulationLenght;
        this.servers = servers;
        this.statistics = statistics;
        this.statistics.registerServers(servers);
        this.fel = new FutureEventList();

        //se inyecta la distribucion de desgaste al primer arribo
        this.fel.insert(
                new Arrival(0, new Entity(0, 0), arrivalBehavior, serviceBehavior, wearBehavior, serverSelectionPolicy));
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
        this.statistics.finishIdleTimes(
        this.servers,
        this.simulationLenght);

        for(Server s : servers) {
            statistics.durationsServers(s.getDurability());
        }
        this.statistics.calculate();
    }
}

/*configuracion para la duracion del evento en 
el sistema dependiendo de la tabla 2 y de la distribucion uniforma
*/

/*
//1. se crean las distribuciones base
Distribution aterrizajeEmpirica = new EmpiricalDiscrete(...); //tabla 2
Distribution descensoUniforme = new Uniform(10, 25); //el descenso

//2. se crea la distribucion combinada que representa el tiempo total en el sistema
Distribution tiempoServicioTotal = new LandingAndDescentDistribution(aterrizajeEmpirica, descensoUniforme);

//3. se le pasa al engine como el comportamiento del servicio (serviceBehavior)
Engine engine = new Engine(
    simulationLength,
    servers,
    arribosTiempoDependiente, //el que hicimos antes para horarios pico
    tiempoServicioTotal,      // <---aca entra la nueva clase
    serverSelectionPolicy,
    statistics
);
*/

/* posible nueva inicializacion para la clase engine 
package mys;

import java.util.List;

import mys.entities.Entity;
import mys.events.Arrival;
import mys.events.Event;
import mys.events.FutureEventList;
import mys.events.Statistics;
import mys.generators.Distribution;
import mys.resources.Server;
import mys.resources.ServerSelectionPolicy;

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
            Distribution wearBehavior, //el nuevo parametro
            ServerSelectionPolicy serverSelectionPolicy,
            Statistics statistics) {

        this.simulationLenght = simulationLenght;
        this.servers = servers;
        this.statistics = statistics;
        this.statistics.registerServers(servers);
        this.fel = new FutureEventList();

        //se inyecta la distribucion de desgaste al primer arribo
        this.fel.insert(
                new Arrival(0, new Entity(0, 0), arrivalBehavior, serviceBehavior, wearBehavior, serverSelectionPolicy));
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

*/