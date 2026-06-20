package mys.policies;

import java.util.List;
import mys.resources.Server;
import mys.resources.ServerSelectionPolicy;

public class ManyServer implements ServerSelectionPolicy {

    @Override
    public Server selectServer(List<Server> servers) {
        // Protección en caso de que la lista esté vacía
        if (servers == null || servers.isEmpty()) {
            throw new IllegalArgumentException("La lista de servidores no puede estar vacía.");
        }

        // =========================================================================
        // PASO 1: Buscar si hay alguna pista (servidor) completamente libre.
        // Si encontramos una libre, la retornamos inmediatamente (así desempatamos 
        // eligiendo la primera según el orden de la lista).
        // =========================================================================
        for (Server server : servers) {
            if (!server.isBusy()) {
                return server; // Terminamos el método acá si hay pista libre
            }
        }

        // =========================================================================
        // PASO 2: Si el código llega acá, significa que TODAS las pistas están ocupadas.
        // Debemos buscar la pista que tenga la cola de espera más corta.
        // =========================================================================
        
        // Inicializamos considerando que la primera pista es nuestra mejor opción actual
        Server bestServer = servers.get(0);
        int minQueueSize = bestServer.queue().size();

        // Recorremos las demás pistas (arrancando desde el índice 1)
        for (int i = 1; i < servers.size(); i++) {
            Server currentServer = servers.get(i);
            int currentQueueSize = currentServer.queue().size();

            // Usamos "<" estrictamente para que, en caso de empate en el tamaño 
            // de la cola, nos quedemos con la primera pista que encontramos.
            if (currentQueueSize < minQueueSize) {
                minQueueSize = currentQueueSize;
                bestServer = currentServer;
            }
        }

        // Retornamos la pista con la cola más corta (o la primera en caso de empate)
        return bestServer;
    }
}
