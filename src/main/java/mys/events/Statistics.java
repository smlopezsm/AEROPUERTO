package mys.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mys.resources.Server;

public class Statistics {

    private double meanSystemTime;
    private double meanWaitingTime;

    private double totalSystemTime = 0;
    private double totalWaitingTime = 0;

    private final Map<Integer, Double> serverTotalIdleTimes = new HashMap<>();
    private final Map<Integer, Double> serverInitIdleTimes = new HashMap<>();

    private double maxSystemTime = 0;
    private double maxWaitingTime = 0;
    //private double minSystemTime = Double.POSITIVE_INFINITY;
    //private double minWaitingTime = Double.POSITIVE_INFINITY;

    private double minSystemTime = Double.MAX_VALUE;
    private double minWaitingTime = Double.MAX_VALUE;
    //private double minIdleTime = Double.MAX_VALUE;
    //private int minQueueLength = Integer.MAX_VALUE;

    private int serverIdMaxIdleTime = -1;
    private double maxIdleTime = 0;
    private double minIdleTime = Double.POSITIVE_INFINITY;
    //private double idleProportion = 0;

    private int totalArrivals = 0;
    private int totalDepartures = 0;
    private double idleProportion;
    
    private int totalAbandoned = 0; 

    private int maxQueueLength = 0;
    private int minQueueLength = Integer.MAX_VALUE;
    //private int totalAbandonedEntities = 0;
    private final List<Double> durationsServers = new ArrayList<>();

    public void registerServers(List<Server> servers) {
        for (Server s : servers){
            this.serverTotalIdleTimes.put(s.id(), 0d);
            this.serverInitIdleTimes.put(s.id(), 0d);
        }
    }

    //getters
    
    public double totalWaitingTime() { return this.totalWaitingTime; }
    
    public double maxSystemTime() { return this.maxSystemTime; }
    public int maxQueueLength() { return this.maxQueueLength; }

    //nuevos getters para minimos (si no se modificaron, devuelven 0 en vez de MAX_VALUE)
    public double minSystemTime() { return this.minSystemTime == Double.MAX_VALUE ? 0 : this.minSystemTime; }
    public double minWaitingTime() { return this.minWaitingTime == Double.MAX_VALUE ? 0 : this.minWaitingTime; }
    public double minIdleTime() { return this.minIdleTime == Double.MAX_VALUE ? 0 : this.minIdleTime; }
    public int minQueueLength() { return this.minQueueLength == Integer.MAX_VALUE ? 0 : this.minQueueLength; }
    
    public int totalAbandoned() { return this.totalAbandoned; }

    //metodos de registro
    public List<Double> servers() {
        return this.durationsServers;
    }

    public void durationsServers(double duration) {
        this.durationsServers.add(duration);
    }
    public double totalIdleTime(int serverId) {
        return this.serverTotalIdleTimes.get(serverId);
    }

    public double totalSystemTime() {
        return this.totalSystemTime;
    }

    public int totalArrivals() {
        return this.totalArrivals;
    }

    public int totalDepartures() {
        return this.totalDepartures;
    }

    public double maxWaitingTime() {
        return this.maxWaitingTime;
    }

   /*  public double minWaitingTime() {
        return finiteMinimum(this.minWaitingTime);
    }*/

    public double maxIdleTime() {
        return this.maxIdleTime;
    }

   /*  public double minIdleTime() {
        return finiteMinimum(this.minIdleTime);
    }*/

    public double serverIdMaxIdleTime() {
        return this.serverIdMaxIdleTime;
    }
    public double meanWaitingTime() {
    return this.meanWaitingTime;
    }

    public double meanSystemTime() {
    return this.meanSystemTime;
   }

    /**
     * alias para el tiempo en el sistema (tiempo de transito)
     */
    public void addTransitTime(double value) {
        this.addSystemTime(value);
    }

    public void addSystemTime(double value) {
        this.totalSystemTime += value;

        if (value > this.maxSystemTime) {
            this.maxSystemTime = value;
        }
       // if (value > 0 && value < this.minSystemTime) { //control distinto de cero
          //  this.minSystemTime = value;
        //}
    }

    /**
     *renombrado a addWaitTime para enlazar correctamente con EndOfService
     */
    public void addWaitTime(double value) {
        this.totalWaitingTime += value;

        if (value > this.maxWaitingTime) {
            this.maxWaitingTime = value;
        }
        if (value > 0 && value < this.minWaitingTime) { //control distinto de cero
            this.minWaitingTime = value;
        }
    }

    //mantenemos el nombre original por si lo usamos en otras partes del codigo
    public void addWaitingTime(double value) {
        this.addWaitTime(value);
    }

    public void addIdleTime(int id, double clock) {
        double idleTime = clock - this.serverInitIdleTimes.get(id);

        this.serverTotalIdleTimes.put(
                id,
                this.serverTotalIdleTimes.get(id) + idleTime);

        if (idleTime > this.maxIdleTime) {
            this.maxIdleTime = idleTime;
            this.serverIdMaxIdleTime = id;
        }
        if (idleTime > 0 && idleTime < this.minIdleTime) { //control distinto de cero
            this.minIdleTime = idleTime;
        }
    }

    public void initIdleTime(int id, double clock) {
        this.serverInitIdleTimes.put(id, clock);
    }

    public void entityArrived() {
        this.totalArrivals++;
    }

    public void entityDeparture() {
        this.totalDepartures++;
    }

    /**
     * registra un avion que abandonó la cola por exceder las 2 horas de espera
     */
    public void addAbandonedEntity() {
        this.totalAbandoned++;
    }

    public void registerQueueLength(int queueLength) {
        if (queueLength > this.maxQueueLength) {
            this.maxQueueLength = queueLength;
        }
        if (queueLength > 0 && queueLength < this.minQueueLength) { //control distinto de cero
            this.minQueueLength = queueLength;
        }
    }

    public double totalIdleTime() {
    return this.serverTotalIdleTimes.values()
            .stream()
            .mapToDouble(Double::doubleValue)
            .sum();
}

public double idleProportion() {
    return this.idleProportion;
}

public void finishIdleTimes(
        List<Server> servers,
        double simulationLength) {

    for (Server server : servers) {
        if (!server.isBusy()) {
            addIdleTime(server.id(), simulationLength);
        }
    }

    double totalAvailableTime =
            simulationLength * servers.size();

    this.idleProportion = totalAvailableTime == 0
            ? 0
            : totalIdleTime() / totalAvailableTime;
}

public int totalAbandonedEntities() {
    return totalAbandoned;}

    public void calculate() {
        //validacion para evitar division por cero si ningun avion logró aterrizar
        if (this.totalDepartures > 0) {
            this.meanWaitingTime = this.totalWaitingTime / this.totalDepartures;
            this.meanSystemTime = this.totalSystemTime / this.totalDepartures;
        }
    }
}
