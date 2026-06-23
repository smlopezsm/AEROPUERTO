package mys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mys.events.Statistics;
import mys.generators.Distribution;
import mys.generators.EmpiricalDiscrete;
import mys.generators.LandingAndDescentDistribution;
import mys.generators.Normal;
import mys.generators.TimeDependentExponential;
import mys.generators.Uniform;
import mys.policies.BalancedServer;
import mys.resources.Server;

/*
en esta etapa se pide implementar una politica de seleccion de pista balanceada por durabilidad, es decir, que seleccione la pista con mayor durabilidad restante. Para esto, se debe modificar el metodo de seleccion de servidor en el motor de simulacion para que tenga en cuenta la durabilidad de cada pista al momento de asignar una aeronave a una pista disponible. Ademas, se deben registrar las durabilidades finales de cada pista al finalizar la simulacion para poder analizarlas posteriormente.
*/

public class StageThreeApp {

    private static final double SIMULATION_LENGTH = 40320;
    private static final int NUMBER_OF_SERVERS = 6;
    private static final int REPLICATIONS = 50;
    private static final long BASE_SEED = 20260621L;
    private static final double T_CRITICAL_95_DF_49 = 2.009575;

    public static void main(String[] args) {
        Map<String, double[]> results = createResultTable();

        for (int replication = 0; replication < REPLICATIONS; replication++) {
            Statistics statistics = runReplication(replication);
            recordResults(results, replication, statistics);
        }

        printReport(results);
    }

    private static Statistics runReplication(int replication) {
        /*
            @param replication: el número de réplica actual (0 a 49)
            @return un objeto Statistics con los resultados de la réplica
        */
        long seed = BASE_SEED + replication * 100L;

        //crear los servidores, las distribuciones y las estadísticas
        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_SERVERS; i++) {
            servers.add(new Server(i, new LinkedList<>()));
        }

        //inicializa las distribucions con semillas distintas para cada replica
        Distribution arrivals = new TimeDependentExponential(15, 9, seed + 1);
        Distribution landing = new EmpiricalDiscrete(
                new double[] {8, 10, 13, 15},
                new double[] {.38, .32, .10, .20},
                seed + 2);
        Distribution service = new LandingAndDescentDistribution(
                landing,
                new Uniform(10, 25, seed + 3));
        Distribution wear = new Normal(5, 1, true, seed + 4);
        Statistics statistics = new Statistics();

        Engine engine = new Engine(
                SIMULATION_LENGTH,
                servers,
                arrivals,
                service,
                wear,
                new BalancedServer(),
                statistics);
        engine.run();
        return statistics; 
    }

    private static Map<String, double[]> createResultTable() {
        Map<String, double[]> results = new LinkedHashMap<>();
        addMetric(results, "Aeronaves arribadas");
        addMetric(results, "Aeronaves aterrizadas");
        addMetric(results, "Transito medio (min)");
        addMetric(results, "Transito maximo (min)");
        addMetric(results, "Transito minimo > 0 (min)");
        addMetric(results, "Espera media (min)");
        addMetric(results, "Espera maxima (min)");
        addMetric(results, "Espera minima > 0 (min)");
        addMetric(results, "Ocio total (min)");
        addMetric(results, "Ocio maximo (min)");
        addMetric(results, "Ocio minimo > 0 (min)");
        addMetric(results, "Proporcion de ocio (%)");
        addMetric(results, "Cola maxima");
        addMetric(results, "Cola minima > 0");
        addMetric(results, "Aeronaves desviadas");
        for (int i = 0; i < NUMBER_OF_SERVERS; i++) {
            addMetric(results, "Durabilidad final pista " + (i + 1));
        }
        return results;
    }

    private static void addMetric(Map<String, double[]> results, String name) {
        results.put(name, new double[REPLICATIONS]);
    }

    private static void recordResults(
            Map<String, double[]> results,
            int replication,
            Statistics statistics) {
        record(results, "Aeronaves arribadas", replication, statistics.totalArrivals());
        record(results, "Aeronaves aterrizadas", replication, statistics.totalDepartures());
        record(results, "Transito medio (min)", replication, statistics.meanSystemTime());
        record(results, "Transito maximo (min)", replication, statistics.maxSystemTime());
        record(results, "Transito minimo > 0 (min)", replication, statistics.minSystemTime());
        record(results, "Espera media (min)", replication, statistics.meanWaitingTime());
        record(results, "Espera maxima (min)", replication, statistics.maxWaitingTime());
        record(results, "Espera minima > 0 (min)", replication, statistics.minWaitingTime());
        record(results, "Ocio total (min)", replication, statistics.totalIdleTime());
        record(results, "Ocio maximo (min)", replication, statistics.maxIdleTime());
        record(results, "Ocio minimo > 0 (min)", replication, statistics.minIdleTime());
        record(results, "Proporcion de ocio (%)", replication, statistics.idleProportion() * 100);
        record(results, "Cola maxima", replication, statistics.maxQueueLength());
        record(results, "Cola minima > 0", replication, statistics.minQueueLength());
        record(results, "Aeronaves desviadas", replication, statistics.totalAbandonedEntities());

        List<Double> durability = statistics.servers();
        for (int i = 0; i < NUMBER_OF_SERVERS; i++) {
            record(results, "Durabilidad final pista " + (i + 1), replication, durability.get(i));
        }
    }

    private static void record(
            Map<String, double[]> results,
            String name,
            int replication,
            double value) {
        results.get(name)[replication] = value;
    }

    private static void printReport(Map<String, double[]> results) {
        System.out.println("===== EXPERIMENTO ETAPA 3 =====");
        System.out.println("Configuracion: 6 pistas, seleccion balanceada por durabilidad");
        System.out.println("Replicas independientes: 50");
        System.out.println("Duracion por replica: 40320 minutos");
        System.out.println("Nivel de confianza: 95% ");
        System.out.println();
        System.out.printf("%-34s %12s %15s %15s%n", "Metrica", "Media", "IC95 inferior", "IC95 superior");

        results.forEach((name, values) -> {
            ConfidenceInterval interval = confidenceInterval(values);
            System.out.printf(
                    "%-34s %12.2f %15.2f %15.2f%n",
                    name,
                    interval.mean(),
                    interval.lower(),
                    interval.upper());
        });
    }

    private static ConfidenceInterval confidenceInterval(double[] values) {
        double mean = Arrays.stream(values).average().orElse(0);
        double squaredDifferences = 0;
        for (double value : values) {
            double difference = value - mean;
            squaredDifferences += difference * difference;
        }

        //usamos n-1 para la varianza muestral
        double standardDeviation = Math.sqrt(squaredDifferences / (values.length - 1));
        double margin = T_CRITICAL_95_DF_49 * standardDeviation / Math.sqrt(values.length);
        return new ConfidenceInterval(mean, mean - margin, mean + margin);
    }

    private record ConfidenceInterval(double mean, double lower, double upper) {
    }
}
 