# TEAM 3

# hacer modelo de dominio (diagrama de clases)

# variables aleatorias entra en el parcial

# Simulador de Sistema de Aterrizaje

# consultar tamaños de pistas, ver que pasa cuando se termina las 4 semanas, estadisticas por pistas o general, durabilidad de las pistas puede ser negativa? o que se hace.

# preguntar si el tam maximo de la cola de espera es cantidad de aviones o cantidad de tiempo?

Simulación de eventos discretos de un sistema de aterrizaje de aviones con una pista.

**Autores:** Ángel Leonel Sanchez Dis, Valentín Luca De Francesco, Santiago López

## Estructura del Proyecto

```
main/src/
├── Main.java                      # Punto de entrada
├── Entidades/
│   └── Avion.java                 # Entidad avión con tiempos de arribo/aterrizaje
├── Estadisticas.java             # Recopilación de estadísticas (Singleton)
├── Eventos/
│   ├── Event.java                 # Interfaz para eventos
│   ├── Arrival.java               # Evento de llegada de avión
│   ├── EndOfService.java          # Evento de fin de servicio (aterrizaje)
│   └── FutureEventList.java       # Lista de eventos futuros (FEL)
├── Generadores/
│   ├── IGeneradorAleatorio.java          # Interfaz para generadores
│   ├── DistribucionEmpiricaDiscreta.java # Distribución empírica discreta
│   ├── DistribucionPoisson.java          # Distribución de Poisson
│   ├── DistribucionNormal.java           # Distribución normal
│   ├── DistribucionUniforme.java         # Distribución uniforme
│   └── DistribucionExponencial.java      # Distribución exponencial
├── Recursos/
│   └── Pista.java                 # Pista con cola de espera
└── Tablas/
    ├── DiscreteveProbability.java       # Interfaz funcional
    ├── TiempoAleatorioTablaUno.java     # Tiempos entre llegadas
    └── TiempoAleatorioTablaDos.java     # Tiempos de aterrizaje
```

## Patrones de Diseño

- **Strategy**: La interfaz `Event` con implementaciones `Arrival` y `EndOfService` permite encapsular algoritmos de eventos intercambiables.
- **Command**: Los eventos encapsulan las acciones a ejecutar mediante el método `execute(Pista, FutureEventList)`.
- **Singleton**: La clase `Estadisticas` utiliza el patrón Singleton (`getInstancia()`) para mantener estadísticas globales de la simulación.
- **Strategy (Generadores)**: La interfaz `IGeneradorAleatorio` permite interchangeable algoritmos de generación de números aleatorios.
- **Priority Queue**: `FutureEventList` actúa como una cola de prioridad ordenando eventos por tiempo de ejecución.

## Distribución de Tiempos

**Tabla 1 - Tiempos entre llegadas:**
| Probabilidad | Tiempo |
|--------------|--------|
| 35% | 10 min |
| 45% | 15 min |
| 20% | 17 min |

**Tabla 2 - Tiempos de aterrizaje:**
| Probabilidad | Tiempo |
|--------------|--------|
| 38% | 8 min |
| 32% | 10 min |
| 10% | 13 min |
| 20% | 15 min |

## Compilación y Ejecución

```bash
cd main/src
javac Main.java
java Main
```

## Modelo

- **Recursos**: 1 pista de aterrizaje
- **Cola**: FIFO (primero en llegar, primero en aterrizar)
- **Eventos**: Arrival (llegada) y EndOfService (fin de servicio)
- **Bootstrap**: El primer evento es una llegada en t=0


## Modelo de Dominio UML


<img src="assets/DiagramaUMLSimulacion.png" alt="Diagrama UML" width="1100"/>
