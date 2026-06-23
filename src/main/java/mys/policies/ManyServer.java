package mys.policies;

import java.util.List;
import mys.resources.Server;
import mys.resources.ServerSelectionPolicy;

public class ManyServer implements ServerSelectionPolicy {

    @Override
    public Server selectServer(List<Server> servers) {
        //por si la lista de servidores es nula o vacia
        if (servers == null || servers.isEmpty()) {
            throw new IllegalArgumentException("La lista de servidores no puede estar vacía.");
        }

        //primer paso: buscar si hay alguna pista libre
        //si encontramos una libre, la retornamos inmediatamente (y asi desempatamos eligiendo la primera según el orden de la lista).
        for (Server server : servers) {
            if (!server.isBusy()) {
                return server; //terminamos el metodo aca si hay pista libre
            }
        }

        //paso 2: si el codigo llega aca, significa que todas las pistas estan ocupadas.
        //tenemos que buscar la pista que tenga la cola de espera mas corta.
        
        //inicializamos considerando que la primera pista es nuestra mejor opcion actual
        Server bestServer = servers.get(0);
        int minQueueSize = bestServer.queue().size();

        //recorremos las demas pistas (arrancando desde el indice 1)
        for (int i = 1; i < servers.size(); i++) {
            Server currentServer = servers.get(i);
            int currentQueueSize = currentServer.queue().size();

            //usamos < para que en caso de empate en el tamaño de la cola, nos quedemos con la primera pista que encontramos.
            if (currentQueueSize < minQueueSize) {
                minQueueSize = currentQueueSize;
                bestServer = currentServer;
            }
        }

        //retornamos la pista con la cola mas corta (o la primera en caso de haber empate)
        return bestServer;
    }
}
//falta inicializar la simulacion con los 5 servidores 