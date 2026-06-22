package mys.generators;
public class TimeDependentExponential implements Distribution {

    private final Exponential normalTraffic;
    private final Exponential peakTraffic;
    private double currentClock; // Estado interno para recibir el reloj

    public TimeDependentExponential(double normalMedia, double peakMedia) {
        // Los tiempos de arribo responden a mu=15 normalmente
        this.normalTraffic = new Exponential(normalMedia);
        // Responden a mu=9 en horarios de fuerte tráfico
        this.peakTraffic = new Exponential(peakMedia);
    }

    public TimeDependentExponential(double normalMedia, double peakMedia, long semilla) {
        this.normalTraffic = new Exponential(normalMedia, semilla);
        this.peakTraffic = new Exponential(peakMedia, semilla + 1);
    }

    // Método para inyectar el tiempo actual antes de pedir el sample
    public void setClock(double clock) {
        this.currentClock = clock;
    }

    @Override
    public double sample() {
        // Calculamos el minuto exacto dentro de un día de 24hs (1440 minutos en total)
        double minutesOfDay = this.currentClock % 1440;

        // Horarios de tráfico fuerte convertidos a minutos:
        // 9 hs * 60 = 540 min  | 13 hs * 60 = 780 min
        boolean isMorningPeak = (minutesOfDay >= 540 && minutesOfDay < 780);

        // 20 hs * 60 = 1200 min | 23 hs * 60 = 1380 min
        boolean isNightPeak = (minutesOfDay >= 1200 && minutesOfDay < 1380);

        // Evaluamos en qué intervalo estamos para devolver el sample correcto
        if (isMorningPeak || isNightPeak) { // 
            return this.peakTraffic.sample(); // Devuelve muestra con mu=9 [cite: 57]
        } else {
            return this.normalTraffic.sample(); // Devuelve muestra con mu=15 [cite: 54]
        }
    }
    
    @Override
    public String toString() {
        return "TimeDependentExponential (Normal mu=" + this.normalTraffic.getMedia() + 
               ", Pico mu=" + this.peakTraffic.getMedia() + ")";
    }
}
