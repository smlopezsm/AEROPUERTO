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
    private double minSystemTime = Double.POSITIVE_INFINITY;
    private double minWaitingTime = Double.POSITIVE_INFINITY;

    // --- NUEVAS VARIABLES PARA MÍNIMOS (Distintos de cero) ---
    private double minSystemTime = Double.MAX_VALUE;
    private double minWaitingTime = Double.MAX_VALUE;
    private double minIdleTime = Double.MAX_VALUE;
    private int minQueueLength = Integer.MAX_VALUE;

    private int serverIdMaxIdleTime = -1;
    private double maxIdleTime = 0;
    private double minIdleTime = Double.POSITIVE_INFINITY;
    private double idleProportion = 0;

    private int totalArrivals = 0;
    private int totalDepartures = 0;
    
    // --- NUEVA VARIABLE PARA ABANDONOS ---
    private int totalAbandoned = 0; 

    private int maxQueueLength = 0;
    private int minQueueLength = Integer.MAX_VALUE;
    private int totalAbandonedEntities = 0;
    private final List<Double> durationsServers = new ArrayList<>();

    public void registerServers(List<Server> servers) {
        for (Server s : servers){
            this.serverTotalIdleTimes.put(s.id(), 0d);
            this.serverInitIdleTimes.put(s.id(), 0d);
        }
    }

    // =========================================================================
    // GETTERS
    // =========================================================================
    
    public double totalWaitingTime() { return this.totalWaitingTime; }
    public double totalIdleTime(int serverId) { return this.serverTotalIdleTimes.get(serverId); }
    public double totalSystemTime() { return this.totalSystemTime; }
    public int totalArrivals() { return this.totalArrivals; }
    public int totalDepartures() { return this.totalDepartures; }
    
    public double maxWaitingTime() { return this.maxWaitingTime; }
    public double maxIdleTime() { return this.maxIdleTime; }
    public double maxSystemTime() { return this.maxSystemTime; }
    public int maxQueueLength() { return this.maxQueueLength; }
    public double serverIdMaxIdleTime() { return this.serverIdMaxIdleTime; }
    
    public double meanWaitingTime() { return this.meanWaitingTime; }
    public double meanSystemTime() { return this.meanSystemTime; }

    // Nuevos getters para mínimos (Si no se modificaron, devuelven 0 en lugar de MAX_VALUE)
    public double minSystemTime() { return this.minSystemTime == Double.MAX_VALUE ? 0 : this.minSystemTime; }
    public double minWaitingTime() { return this.minWaitingTime == Double.MAX_VALUE ? 0 : this.minWaitingTime; }
    public double minIdleTime() { return this.minIdleTime == Double.MAX_VALUE ? 0 : this.minIdleTime; }
    public int minQueueLength() { return this.minQueueLength == Integer.MAX_VALUE ? 0 : this.minQueueLength; }
    
    public int totalAbandoned() { return this.totalAbandoned; }

    // =========================================================================
    // MÉTODOS DE REGISTRO
    // =========================================================================
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

    public double minWaitingTime() {
        return finiteMinimum(this.minWaitingTime);
    }

    public double maxIdleTime() {
        return this.maxIdleTime;
    }

    public double minIdleTime() {
        return finiteMinimum(this.minIdleTime);
    }

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
     * Alias para el tiempo en el sistema, ya que el PDF lo llama "tiempo de tránsito".
     */
    public void addTransitTime(double value) {
        this.addSystemTime(value);
    }

    public void addSystemTime(double value) {
        this.totalSystemTime += value;

        if (value > this.maxSystemTime) {
            this.maxSystemTime = value;
        }
       // if (value > 0 && value < this.minSystemTime) { // Control distinto de cero
          //  this.minSystemTime = value;
        }
    }

    /**
     * Renombrado a addWaitTime para enlazar correctamente con EndOfService
     */
    public void addWaitTime(double value) {
        this.totalWaitingTime += value;

        if (value > this.maxWaitingTime) {
            this.maxWaitingTime = value;
        }
        if (value > 0 && value < this.minWaitingTime) { // Control distinto de cero
            this.minWaitingTime = value;
        }
    }

    // Mantenemos el nombre original por si lo usabas en otras partes de tu código
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
        if (idleTime > 0 && idleTime < this.minIdleTime) { // Control distinto de cero
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
     * Registra un avión que abandonó la cola por exceder las 2 horas de espera
     */
    public void addAbandonedEntity() {
        this.totalAbandoned++;
    }

    public void registerQueueLength(int queueLength) {
        if (queueLength > this.maxQueueLength) {
            this.maxQueueLength = queueLength;
        }
        if (queueLength > 0 && queueLength < this.minQueueLength) { // Control distinto de cero
            this.minQueueLength = queueLength;
        }
    }

    public void calculate() {
        // Validación para evitar división por cero si ningún avión logró aterrizar
        if (this.totalDepartures > 0) {
            this.meanWaitingTime = this.totalWaitingTime / this.totalDepartures;
            this.meanSystemTime = this.totalSystemTime / this.totalDepartures;
        }
    }
}
