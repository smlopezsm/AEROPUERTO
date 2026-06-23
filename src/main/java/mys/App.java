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
        
        //1. configuracion de 4 semanas de simulacion en minutos (4 * 7 * 24 * 60)
    

        //2. instanciar los 5 servidores (pistas) con sus colas
        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_SERVERS; i++) {
            servers.add(new Server(i, new LinkedList<>()));
        }

        //3. distribucion de arribos: dependiente del tiempo (normal 15, pico 9)
        Distribution arrivalBehavior = new TimeDependentExponential(15.0, 9.0);

        //4. distribucion de servicio: aterrizaje (tabla 2) + descenso (uniforme)
        double[] landingValues = {8.0, 10.0, 13.0, 15.0};
        double[] landingProbs = {0.38, 0.32, 0.10, 0.20};
        Distribution aterrizajeEmpirica = new EmpiricalDiscrete(landingValues, landingProbs); //ajusta segun el constructor de la clase
        Distribution descensoUniforme = new Uniform(10.0, 25.0);
        
        Distribution serviceBehavior = new LandingAndDescentDistribution(aterrizajeEmpirica, descensoUniforme);

        //5. distribucion de desgaste: normal(μ=5, σ=1), truncada a positivos
        Distribution wearBehavior = new Normal(5.0, 1.0, true);

        //6. politica de servidores: multiserver (cola mas corta)
        ServerSelectionPolicy serverSelectionPolicy = new ManyServer();

        //7. estadisticas
        Statistics statistics = new Statistics();

        //8. inyectar todo al engine
        Engine engine = new Engine(
                SIMULATION_LENGTH,
                servers,
                arrivalBehavior,
                serviceBehavior,
                wearBehavior, //inyectamos el comportamiento de desgaste
                serverSelectionPolicy,
                statistics
        );

        //ejecutar simulacion
        engine.run();
        new TerminalReport().report(engine.statistics());
        
    }
}
