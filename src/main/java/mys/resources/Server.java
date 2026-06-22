package mys.resources;

import java.util.Queue;

import mys.entities.Entity;

public class Server {

    private final int id;
    private final Queue<Entity> queue;
    private Entity entity;
    private double durability; 

    public Server(int id, Queue<Entity> queue) {
        this.id = id;
        this.queue = queue;
        this.entity = null;
        this.durability = 3000.0; // Valor inicial definido en el modelo
    }

    /**
     * returns the id of the server
     * @return the id of the server
     */
    public int id(){
        return this.id;
    }

    /**
     * returns true if the server is busy, false otherwise
     * @return true if the server is busy, false otherwise
     */
    public boolean isBusy(){
        return this.entity != null;
    }

    /**
     * sets the entity into the server and makes it busy
     * @param e the entity to be set into the server.
     */
    public void entity(Entity e){
        this.entity = e;
    }

    /**
     * returns the queue of the server
     * @return the queue of the server
     */
    public Queue<Entity> queue(){
        return this.queue;
    }

    /**
     * Devuelve la durabilidad actual de la pista
     */
    public double getDurability() {
        return this.durability;
    }

    /**
     * Resta un valor a la durabilidad actual de la pista
     * @param wearAmount La cantidad de desgaste a aplicar
     */
    public void decreaseDurability(double wearAmount) {
        this.durability -= wearAmount;
        }
}