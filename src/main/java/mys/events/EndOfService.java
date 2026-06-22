package mys.events;

import java.util.List;

import mys.entities.Entity;
import mys.generators.Distribution;
import mys.resources.Server;

public class EndOfService implements Event {

    private final double clock;
    private final int order = 0; // Prioridad más alta que el Arrival (100) para procesar salidas primero en caso de empate de reloj
    private final Entity entity;
    private final Distribution serviceDistribution;
    private final Distribution wearDistribution; // Se añade la distribución de desgaste

    // Constructor actualizado para recibir la distribución de desgaste
    public EndOfService(double clock, Entity entity, Distribution serviceDistribution, Distribution wearDistribution) {
        this.clock = clock;
        this.entity = entity;
        this.serviceDistribution = serviceDistribution;
        this.wearDistribution = wearDistribution;
    }

    @Override
    public void planificate(FutureEventList fel, List<Server> servers, Statistics statistics) {
        
        
        Server server = this.entity().server();
        
        //Marcar las estadísticas de los aterrizajes


        // --- Registro de Estadísticas de la Entidad que finaliza ---
        // Aquí puedes calcular el tiempo de tránsito (tiempo total en el sistema)
        // del avión que acaba de aterrizar y reportarlo a statistics.
        // statistics.addTransitTime(this.clock - this.entity().arrivalTime());
        
        // 1. Calculamos y aplicamos el desgaste a la pista
        double wearAmount = this.wearDistribution.sample();
        server.decreaseDurability(wearAmount);
        
        // 2. Liberamos el servidor del avión que acaba de aterrizar
        server.entity(null);
        this.entity().server(null);
        
        // =================================================================
        // LÓGICA DE LÍMITE DE ESPERA (2 HORAS = 120 MINUTOS)
        // =================================================================
        // Preguntarle al dani, si esta bien esta configuracion

        /*
            Postulacion, che no te podemos atender en 2 horas, te vas a quedar sin nafta y te vas a cagar muriendo 
            andate a otro aeropuerto.
        */
        boolean planeAssigned = false;

        // Recorremos la cola hasta encontrar un avión válido o vaciarla
        while (!server.queue().isEmpty() && !planeAssigned) {
            
            Entity nextPlane = server.queue().poll();
            
            // Calculamos cuánto tiempo pasó este avión en la cola.
            double waitTime = this.clock() - nextPlane.arrivalTime(); 
            
            if (waitTime <= 120.0) {
                // El avión es válido (esperó 2 horas o menos). Entra a la pista.
                server.entity(nextPlane);
                nextPlane.server(server);
                
                // Registramos el tiempo de espera en las estadísticas antes de que inicie su servicio
                statistics.addWaitTime(waitTime);
                
                // Planificamos su fin de servicio
                fel.insert(new EndOfService(
                        this.clock() + this.serviceDistribution.sample(), 
                        nextPlane, 
                        this.serviceDistribution, 
                        this.wearDistribution));
                
                planeAssigned = true; // Cortamos el bucle while
                
            } else {
                // El avión expiró. Superó los 120 minutos en la cola y se desvió a otro aeropuerto.
                // Lo contamos en las estadísticas para tu reporte final.
                statistics.addAbandonedEntity(); 
            }
        }
    }

    @Override
    public double clock() {
        return this.clock;
    }

    @Override
    public int order() {
        return this.order;
    }

    @Override
    public Entity entity() {
        return this.entity;
    }

    @Override
    public Distribution distribution() {
        return this.serviceDistribution;
    }
}