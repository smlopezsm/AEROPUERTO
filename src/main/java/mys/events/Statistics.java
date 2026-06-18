package mys.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mys.resources.Server;

public class Statistics {

    // private final double meanSystemTime;
    private double meanWaitingTime;

    private double totalSystemTime = 0;
    private double totalWaitingTime = 0;

    private Map<Integer, Double> serverTotalIdleTimes = new HashMap<>();
    private Map<Integer, Double> serverInitIdleTimes = new HashMap<>();

    private double maxSystemTime = 0;
    private double maxWaitingTime = 0;

    private int serverIdMaxIdleTime = -1;
    private double maxIdleTime = 0;

    private int totalArrivals = 0;
    private int totalDepartures = 0;

    void registerServers(List<Server> servers) {
        for (Server s : servers)
            this.serverTotalIdleTimes.put(s.id(), 0d);
    }

    public double totalWaitingTime() {
        return this.totalWaitingTime;
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

    public double maxIdleTime() {
        return this.maxIdleTime;
    }

    public double serverIdMaxIdleTime() {
        return this.serverIdMaxIdleTime;
    }

    /**
     * 
     * @return the max system time registered
     */
    public double maxSystemTime() {
        return this.maxSystemTime;
    }

    /**
     * 
     * @return the max queue lenght registered
     */
    public int maxQueueLength() {
        return 0;
    }

    /**
     * add the system time of an entity to the total system time, and check if it's
     * greater than the max system time
     * 
     * @param value the system time of an entity
     */
    public void addSystemTime(double value) {

        this.totalSystemTime += value;

        if (value > this.maxSystemTime)
            this.maxSystemTime = value;
    }

    /**
     * add the waiting time of an entity to the total waiting time, and check if
     * it's greater than the max waiting time
     * 
     * @param value the waiting time of an entity
     */
    public void addWaitingTime(double value) {
        this.totalWaitingTime += value;

        if (value > this.maxWaitingTime)
            this.maxWaitingTime = value;
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

    public void calculate() {
        this.meanWaitingTime = this.totalWaitingTime/this.totalDepartures;
    }
}
