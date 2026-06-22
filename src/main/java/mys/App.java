/*package mys;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mys.events.Statistics;
import mys.policies.OneServer;
import mys.resources.Server;
import mys.statistics.TerminalReport;
import mys.tables.TiempoAleatorioTablaDos;
import mys.tables.TiempoAleatorioTablaUno;
public class App {

    private static final double SIMULATION_LENGTH = 40320;
    private static final int NUMBER_OF_SERVERS = 1;

    public static void main(String[] args) {

        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_SERVERS; i++)
            servers.add(new Server(i, new LinkedList<>()));

        
        Engine engine = new Engine(
                SIMULATION_LENGTH,
                servers,
                new TiempoAleatorioTablaUno(),
                new TiempoAleatorioTablaDos(),
                new OneServer(),
                new Statistics());

        engine.run();

        new TerminalReport().report(engine.statistics());

    }
}*/
/*
    Nota sobre la inicialización: 
    Cuando configures tu simulador en tu archivo App.java (o donde instancies el Engine), en 
    lugar de pasar un new Exponential(15) como arrivalBehavior, 
    simplemente pasarás new TimeDependentExponential(15.0, 9.0).
*/


// nueva inicializacion posible para la clase
package mys;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mys.events.Statistics;
import mys.generators.Distribution;
import mys.generators.EmpiricalDiscrete;
import mys.generators.LandingAndDescentDistribution;
import mys.generators.Normal;
import mys.generators.TimeDependentExponential;
import mys.generators.Uniform;
import mys.policies.ManyServer;
import mys.resources.Server;
import mys.resources.ServerSelectionPolicy;
import mys.statistics.TerminalReport;

public class App {
    private static final double SIMULATION_LENGTH = 40320;
    private static final int NUMBER_OF_SERVERS = 5;
    public static void main(String[] args) {
        
        // 1. Configuración de 4 semanas de simulación en minutos (4 * 7 * 24 * 60)
    

        // 2. Instanciar los 5 servidores (pistas) con sus colas
        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_SERVERS; i++) {
            servers.add(new Server(i, new LinkedList<>()));
        }

        // 3. Distribución de Arribos: Dependiente del tiempo (Normal 15, Pico 9)
        Distribution arrivalBehavior = new TimeDependentExponential(15.0, 9.0);

        // 4. Distribución de Servicio: Aterrizaje (Tabla 2) + Descenso (Uniforme)
        double[] landingValues = {8.0, 10.0, 13.0, 15.0};
        double[] landingProbs = {0.38, 0.32, 0.10, 0.20};
        Distribution aterrizajeEmpirica = new EmpiricalDiscrete(landingValues, landingProbs); // Ajusta según el constructor de tu clase
        Distribution descensoUniforme = new Uniform(10.0, 25.0);
        
        Distribution serviceBehavior = new LandingAndDescentDistribution(aterrizajeEmpirica, descensoUniforme);

        // 5. Distribución de Desgaste: Normal(μ=5, σ=1), truncada a positivos
        Distribution wearBehavior = new Normal(5.0, 1.0, true);

        // 6. Política de servidores: Multiserver (cola más corta)
        ServerSelectionPolicy serverSelectionPolicy = new ManyServer();

        // 7. Estadísticas
        Statistics statistics = new Statistics();

        // 8. Inyectar todo al Engine
        Engine engine = new Engine(
                SIMULATION_LENGTH,
                servers,
                arrivalBehavior,
                serviceBehavior,
                wearBehavior, // Inyectamos el comportamiento de desgaste
                serverSelectionPolicy,
                statistics
        );

        // Ejecutar simulación
        engine.run();
        new TerminalReport().report(engine.statistics());
        
    }
}
