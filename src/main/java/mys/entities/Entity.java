package entities;

public class Entity {

    private int id;
    private double arrivalTime;
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


}
