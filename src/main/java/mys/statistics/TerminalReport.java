package mys.statistics;

import mys.events.Statistics;
public class TerminalReport implements ReportManager {

    @Override
    public void report(Statistics statistics) {
        System.out.println("===== Reporte de simulacion =====");
        System.out.println("Total de arribos: " + statistics.totalArrivals());
        System.out.println("Total de salidas: " + statistics.totalDepartures());
        System.out.println("Tiempo total de espera: " + statistics.totalWaitingTime());
        System.out.println("Tiempo total en el sistema: " + statistics.totalSystemTime());
        System.out.println("Tiempo maximo de espera: " + statistics.maxWaitingTime());
        System.out.println("Tiempo maximo en el sistema: " + statistics.maxSystemTime());
        System.out.println("Tiempo maximo de ocio: " + statistics.maxIdleTime());
        System.out.println("Servidor con mayor tiempo de ocio: " + statistics.serverIdMaxIdleTime());
        System.out.println("Longitud maxima de cola: " + statistics.maxQueueLength());
        System.out.printf("Tiempo promedio de espera: %.2f%n", statistics.meanWaitingTime());
        System.out.printf("Tiempo promedio en el sistema: %.2f%n", statistics.meanSystemTime());
    }

}
