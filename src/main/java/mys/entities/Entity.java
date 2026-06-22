package mys.entities;

import mys.resources.Server;

public class Entity {

    //se agrega 'final' porque la identidad y la hora de llegada de un avión no cambian nunca una vez que el avión fue creado.
    private final int id;
    private final double arrivalTime;
    
    // Este NO es final porque el avión entra y sale de los servidores
    private Server server;

    public Entity(int id, double arrivalTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
    }

    public int id() {
        return id;
    }

    public double arrivalTime() {
        return arrivalTime;
    }

    public Server server() {
        return server;
    }

    public void server(Server server) {
        this.server = server;
    }
    @Override
    public String toString() {
        return String.format("Aeronave[ID: %d, Llegada: %.2f]", this.id, this.arrivalTime);
    }
}