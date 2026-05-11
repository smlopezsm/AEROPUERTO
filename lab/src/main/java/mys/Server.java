package mys;

import java.util.Queue;

public class Server {

    private final int id;
    private final Queue<Entity> queue;
    private Entity entity;

    public Server(int id, Queue<Entity> queue) {
        this.id = id;
        this.queue = queue;
        this.entity = null;
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
}
