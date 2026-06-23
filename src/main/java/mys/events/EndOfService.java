package mys.events;

import java.util.List;

import mys.entities.Entity;
import mys.generators.Constant;
import mys.generators.Distribution;
import mys.resources.Server;

public class EndOfService implements Event {

    private final double clock;
    private final int order = 0; //prioridad mas alta que el arrival (100) para procesar salidas primero en caso de empate de reloj
    private final Entity entity;
    private final Distribution serviceDistribution;
    private final Distribution wearDistribution; //se añade la distribucion de desgaste

    public EndOfService(double clock, Entity entity, Distribution serviceDistribution) {
        this.clock = clock;
        this.entity = entity;
        this.serviceDistribution = serviceDistribution;
        this.wearDistribution = new Constant(0); //se asigna null si no se proporciona desgaste
    }
    //constructor actualizado para recibir la distribución de desgaste
    public EndOfService(double clock, Entity entity, Distribution serviceDistribution, Distribution wearDistribution) {
        this.clock = clock;
        this.entity = entity;
        this.serviceDistribution = serviceDistribution;
        this.wearDistribution = wearDistribution;
    }

    @Override
    public void planificate(FutureEventList fel, List<Server> servers, Statistics statistics) {
        
        
        Server server = this.entity().server();

        statistics.entityDeparture();
        statistics.addSystemTime(this.clock - this.entity().arrivalTime());

        
        
        //marcar las estadísticas de los aterrizajes


        //--- registro de Estadísticas de la entidad que finaliza ---
        //aca se puede calcular el tiempo de transito (tiempo total en el sistema) del avión que acaba de aterrizar y reportarlo a statistics.
        //statistics.addTransitTime(this.clock - this.entity().arrivalTime());
        
        //1. calculamos y aplicamos el desgaste a la pista
        double wearAmount = this.wearDistribution.sample();
        server.decreaseDurability(wearAmount);
        
        //2. liberamos el servidor del avion que acaba de aterrizar
        server.entity(null);
        this.entity().server(null);
        
        //LOGICA DE LIMITE DE ESPERA (2 horas = 120 minutos)
        //preguntarle al dani, si esta bien esta configuracion

        /*
            postulacion: che no te podemos atender en 2 horas, te vas a quedar sin nafta y te vas a cagar muriendo 
            andate a otro aeropuerto.
        */
        boolean planeAssigned = false;

        //recorremos la cola hasta encontrar un avion valido o vaciarla
        while (!server.queue().isEmpty() && !planeAssigned) {
            
            Entity nextPlane = server.queue().poll();
            
            //calculamos cuanto tiempo paso este avion en la cola.
            double waitTime = this.clock() - nextPlane.arrivalTime(); 
            
            if (waitTime <= 120.0) {
                //el avion es valido (espero 2 horas o menos). Entra a la pista.
                server.entity(nextPlane);
                nextPlane.server(server);
                
                //registramos el tiempo de espera en las estadisticas antes de que inicie su servicio
                statistics.addWaitingTime(waitTime);
                
                //planificamos su fin de servicio
                fel.insert(new EndOfService(
                        this.clock() + this.serviceDistribution.sample(), 
                        nextPlane, 
                        this.serviceDistribution, 
                        this.wearDistribution));
                
                planeAssigned = true; //cortamos el bucle while
                
            } else {
                //el avion expiró. Superó los 120 minutos en la cola y se desvió a otro aeropuerto.
                //lo contamos en las estadisticas para tu reporte final.
            ;
                statistics.addAbandonedEntity(); 
            }
        }
        if (!server.isBusy()) {
    statistics.initIdleTime(server.id(), this.clock);
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