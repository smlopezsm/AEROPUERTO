package mys.statistics;

import mys.events.Statistics;
public class TerminalReport implements ReportManager {

    @Override
    public void report(Statistics statistics) {
        System.out.println("===== Reporte de simulacion =====");
        System.out.println("Total de arribos: " + statistics.totalArrivals());
        System.out.println("Total de salidas: " + statistics.totalDepartures());
        System.out.printf("Tiempo total de espera: %.2f%n ", statistics.totalWaitingTime());
        System.out.printf("Tiempo total en el sistema: %.2f%n ", statistics.totalSystemTime());
        System.out.printf("Tiempo maximo de espera: %.2f%n ",  statistics.maxWaitingTime());
        System.out.printf("Tiempo maximo en el sistema: %.2f%n ", statistics.maxSystemTime());
        System.out.printf("Tiempo maximo de ocio: %.2f%n ", statistics.maxIdleTime());
        System.out.println("Servidor con mayor tiempo de ocio: " + (statistics.serverIdMaxIdleTime()+1));
        System.out.printf("Longitud maxima de cola: %d%n", statistics.maxQueueLength());
        System.out.printf("Tiempo promedio de espera: %.2f%n", statistics.meanWaitingTime());
        System.out.printf("Tiempo promedio en el sistema: %.2f%n", statistics.meanSystemTime());
        System.out.printf(
        "Tiempo total de ocio: %.2f%n",
        statistics.totalIdleTime());

        System.out.printf(
        "Proporcion total de ocio: %.2f%%%n",
        statistics.idleProportion() * 100);        statistics.servers().forEach(durability -> System.out.printf("Durabilidad final de la pista: %.2f%n", durability));
    System.out.println(
        "Aeronaves desviadas por superar 120 minutos: "
        + statistics.totalAbandonedEntities());}
        
    }


