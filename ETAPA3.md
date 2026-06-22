# Etapa 3 - Replicacion y analisis

## Configuracion propuesta

Se utilizan seis pistas y una politica de seleccion balanceada. Entre las pistas
libres se elige la de mayor durabilidad. Si todas estan ocupadas, se elige la
cola mas corta y la durabilidad se usa como desempate.

Esta configuracion mejora el modelo anterior porque evita concentrar todos los
aterrizajes en las primeras pistas. Tambien mantiene la espera por debajo de
120 minutos y evita que la durabilidad final sea negativa.

## Procedimiento

1. Se ejecutaron 50 replicas independientes.
2. Cada replica simulo 40320 minutos, equivalentes a cuatro semanas.
3. Cada replica uso semillas diferentes y reproducibles.
4. En cada replica se registraron arribos, aterrizajes, tiempos, ocio, colas,
   desvios y durabilidad final.
5. Para cada parametro se calculo la media muestral y el desvio estandar.
6. El intervalo de confianza se calculo con t de Student para 49 grados de
   libertad y un nivel de confianza del 95%.

Formula utilizada:

```text
IC95 = media +/- 2.009575 * desvio_muestral / sqrt(50)
```

## Resultados

| Metrica | Media | IC95 inferior | IC95 superior |
|---|---:|---:|---:|
| Aeronaves arribadas | 3210.26 | 3195.09 | 3225.43 |
| Aeronaves aterrizadas | 3208.54 | 3193.34 | 3223.74 |
| Transito medio (min) | 28.41 | 28.37 | 28.44 |
| Transito maximo (min) | 61.89 | 60.49 | 63.29 |
| Transito minimo mayor a cero (min) | 18.01 | 18.01 | 18.01 |
| Espera media (min) | 0.37 | 0.35 | 0.40 |
| Espera maxima (min) | 31.39 | 29.98 | 32.79 |
| Espera minima mayor a cero (min) | 0.10 | 0.07 | 0.13 |
| Ocio total (min) | 151947.28 | 151522.19 | 152372.37 |
| Ocio maximo (min) | 250.40 | 243.30 | 257.50 |
| Ocio minimo mayor a cero (min) | 0.02 | 0.02 | 0.03 |
| Proporcion de ocio (%) | 62.81 | 62.63 | 62.98 |
| Cola maxima | 1.16 | 1.05 | 1.27 |
| Cola minima mayor a cero | 1.00 | 1.00 | 1.00 |
| Aeronaves desviadas | 0.00 | 0.00 | 0.00 |
| Durabilidad final pista 1 | 329.35 | 316.43 | 342.26 |
| Durabilidad final pista 2 | 329.17 | 316.21 | 342.12 |
| Durabilidad final pista 3 | 329.10 | 316.17 | 342.02 |
| Durabilidad final pista 4 | 328.80 | 315.79 | 341.81 |
| Durabilidad final pista 5 | 328.74 | 315.87 | 341.60 |
| Durabilidad final pista 6 | 328.90 | 316.03 | 341.78 |

## Conclusion

La configuracion no produjo aeronaves desviadas en ninguna de las 50 replicas.
La espera maxima estimada se mantuvo muy por debajo de 120 minutos y el
desgaste quedo distribuido de forma uniforme. El costo de esta mejora es una
mayor proporcion de ocio, cercana al 62.81% de la capacidad total.

## Ejecucion

```powershell
java -cp target\check mys.StageThreeApp
```
