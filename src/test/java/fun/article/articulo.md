## Introducción

El universo de los generadores de números pseudoaleatorios es fascinante y, a la vez, un terreno
complejo. Si te apasionan las matemáticas, es definitivamente un campo que te va a encantar
explorar. En este artículo, nos sumergiremos en su aplicación práctica para realizar pruebas, ya
sean unitarias o de integración.

La utilización de generadores de datos pseudoaleatorios en el proceso de testing proporciona una
serie de ventajas sustanciales que pueden mejorar significativamente la eficacia y la calidad de las
pruebas. Aquí detallo algunas de las innumerables ventajas:

-   Fácil generación de datos de prueba. Los generadores simplifican la creación de datos de prueba
    al automatizar el proceso. En lugar de tener que definir manualmente cada conjunto de datos, los
    generadores permiten especificar reglas y criterios, generando automáticamente casos de prueba.

-   Mantenibilidad mejorada. La modularidad inherente a los generadores facilita la adaptación y
    actualización de casos de prueba. Al centralizar la lógica de generación en un generador
    específico, cualquier cambio en los requisitos de prueba puede abordarse de manera más eficiente
    y coherente.

-   Random Testing (Pruebas Aleatorias). La capacidad de generar datos de prueba de manera aleatoria
    a través de generadores es valiosa para descubrir posibles problemas que podrían no ser
    evidentes en pruebas más estáticas. Este enfoque aleatorio puede revelar escenarios inesperados
    y mejorar la robustez de la aplicación.

-   Property-Based Testing (Pruebas Basadas en Propiedades). Los generadores son fundamentales para
    la implementación de pruebas basadas en propiedades. En lugar de centrarse en casos de prueba
    específicos, estas pruebas definen propiedades que deben cumplir todas las entradas generadas
    por un generador. Esto facilita la identificación de errores y comportamientos inesperados en un
    conjunto amplio de datos.

-   Mayor cobertura de código. La automatización de la generación de datos permite realizar pruebas
    exhaustivas con una amplia variedad de entradas, aumentando la probabilidad de descubrir
    problemas en diferentes partes del código.

-   Reproducibilidad. La capacidad de reproducir casos de prueba específicos mediante la fijación de
    semillas en generadores asegura la consistencia en las pruebas. Esto es especialmente valioso
    para la depuración y la colaboración en entornos de desarrollo.

-   Eficiencia en el desarrollo de pruebas. Al utilizar generadores, se reduce la carga de trabajo
    manual asociada con la creación y mantenimiento de conjuntos de datos de prueba, lo que permite
    a los desarrolladores y equipos de QA centrarse en la calidad y eficacia de las pruebas en lugar
    de la manipulación directa de los datos de prueba.

En resumen, la utilización de generadores no solo simplifica el proceso de testing, sino que también
proporciona un enfoque más robusto y eficiente para garantizar la calidad del software.

## ¿Aleatorio o Pseudoaleatorio? Entendiendo las Diferencias

En un sentido puro, los números aleatorios son impredecibles y no siguen ningún patrón discernible.
Son el resultado de eventos naturales o procesos caóticos, como el lanzamiento de un dado, el ruido
atmosférico o un bombo expulsando bolas. En el ámbito computacional, generar números verdaderamente
aleatorios es un desafío, ya que los ordenadores son sistemas deterministas y predecibles por
naturaleza. Es curioso el caso de Cloudflare, que dispone de una pared de
[lámparas de lava](https://blog.cloudflare.com/lavarand-in-production-the-nitty-gritty-technical-details/)
que utiliza como fuente de aleatoriedad para diferentes procesos criptográficos.

Dada la naturaleza determinista de los ordenadores, se recurre a los generadores pseudoaleatorios
para simular la aleatoriedad. Estos generadores utilizan algoritmos para producir secuencias de
números que, aunque parecen aleatorios, son completamente predecibles si se conoce la semilla
inicial. La "semilla" es un valor de entrada que inicia el proceso de generación y determina
completamente la secuencia resultante.

Algunas características clave de los generadores pseudoaleatorios son:

1. **Reproducibilidad:** Una misma semilla siempre generará la misma secuencia de números, lo que es
   beneficioso para pruebas y depuración.

2. **Periodo:** Dado que los algoritmos son finitos, las secuencias eventualmente se repiten,
   formando un ciclo. La longitud de este ciclo depende del algoritmo y la semilla.

3. **Eficiencia:** A diferencia de los verdaderamente aleatorios, los generadores pseudoaleatorios
   son eficientes y pueden generar secuencias en cualquier momento.

¿Pero por qué pseudoaleatorio y no aleatorio?

La elección de utilizar generadores pseudoaleatorios en lugar de números aleatorios reales radica en
la necesidad de reproducibilidad y control. En aplicaciones prácticas, como simulaciones y juegos,
es crucial poder replicar resultados y compartir escenarios específicos, lo que se logra fácilmente
con generadores pseudoaleatorios. En nuestro caso de uso relacionado con las pruebas, dicha
reproducibilad es también clave. Imagina generar un dato que produzca un error y no ser capaz de
volver a generarlo. ¡Qúe mala suerte!

Espero que hayas entendido las diferencias y el porqué la loteria del Niño de Navidad que se celebra
aquí en España el día 23 de diciembre debe de ser de lo poco que no corre peligro con la
Inteligencia Artificial, o quizás también...

## ¿Cómo modelamos un generador en Java?

Es relevante destacar que en la versión 17, Java implementó significativas mejoras en relación con
los generadores de números pseudoaleatorios (PRNG). Una de las innovaciones clave fue la
introducción de la interfaz `RandomGenerator`. Hasta ese momento, no existía una interfaz común, y
era común encontrarse directamente con alguna de las implementaciones habituales como `Random` o
`SecureRandom`. No obstante, dado que los tiempos evolucionan y surgen nuevas necesidades junto con
nuevas implementaciones, la ausencia de una interfaz común planteaba desafíos en el desarrollo y
mantenimiento del código.

Para comenzar, vamos a abordar cómo modelar un PRNG en Java pero siguiendo un enfoque funcional.
Esto nos abrirá las puertas para, a partir de generadores de datos primitivos (números, texto,
boolean etc.), crear de manera sencilla generadores de estructuras de datos complejas, tales como
récords, JSON, o incluso cualquier objeto de nuestro modelo de datos.

Como hemos comentado, necesitamos un estado inicial o semilla a partir de la cual crear una fuente
de aleatoriedad para generar las secuencias deseadas. En versiones anteriores a Java 17, podríamos
haber utilizado una implementación específica, por ejemplo, `Random`, de la siguiente manera:

```code

import java.util.Random;

long seed = 100_000_000L;

Random random = new Random(seed);

```

No obstante, para generalizar y aprovechar las mejoras introducidas en Java 17, optaremos por
utilizar `RandomGenerator`, una interfaz que nos permite recuperar la implementación real (que puede
variar según la versión de la JDK y/o el sistema operativo):

```code

import java.util.random.RandomGenerator;

RandomGenerator random = RandomGenerator.getDefault();

System.out.println(random.getClass().getName());

```

En mi caso, el código anterior imprime `L32X64MixRandom`. La introducción de nuevos algoritmos en la
versión 17 de Java responde a la evolución de las necesidades. Por ejemplo periodos de 2^32 e
incluso 2^64 resultan insuficientes en muchos contextos actuales por razones de seguridad. ¡Y es que
el generador L32X64MixRandom tiene un periodo de 2^96!

Volviendo a nuestro caso de modelado, ¿qué os parece la siguiente propuesta?

```code

public interface Gen<O> extends Function<RandomGenerator, Supplier<O>> { }

```

Un generador de tipo `0` es una función que tiene como input una fuente de aleatoriedad (a su vez
creada a partir de una semilla), y como output un productor (o supplier) de objetos de tipo `O`. Los
suppliers son fundamentales en Java porque introducen lo que se conoce como lazyness, es decir hasta
que no se invoque de forma explícita su método `get` ningún elemento será generado.

Vamos a definir una serie de métodos en nuestra interfaz `Gen` que utilizaremos constantemente:

```java

public interface Gen<O> extends Function<RandomGenerator, Supplier<O>> {

    default Supplier<O> sample();

    default <P> Gen<P> map(Function<O, P> fn);

    default <P> Gen<P> then(Function<O, Gen<P>> fn);

    default Map<String, Long> classify(int n,
                                       Map<String, Predicate<O>> classifier,
                                       String defaultLabel
                                      );

    default Map<O, Long> collect(int n);


}

```

-   `sample()` es simplemente syntactic sugar para obtener un supplier a partir de la implementación
    por defecto asignada a la versión de la JVM que estemos usando

```code
Gen<O> gen = ???

Supplier<O> xs = gen.apply(RandomGenerator.getDefault());

Supplier<O> ys = gen.sample();

```

-   `classify(int n, Map<String, Predicate<O>> classifiers, String defaultLabel)` permite clasificar
    los valores generados en categorías definidas por el usuario. Genera `n` instancias y las asigna
    a las categorías especificadas en las claves del `Map` si se cumple el predicado asociado. El
    parámetro `defaultLabel` se utiliza para asignar valores a categorías no especificadas,
    ofreciendo flexibilidad en la clasificación de los resultados.

Antes de continuar vamos a resumir de forma concisa los principales puntos vistos hasta ahora

-   Los generadores de números pseudoaleatorios son fundamentales para el testing, proporcionando
    ventajas como la fácil generación de datos de prueba, mantenibilidad mejorada y mayor cobertura
    de código.
-   Random Testing y Property-Based Testing son enfoques valiosos facilitados por generadores.

-   La naturaleza determinista de las computadoras hace que los generadores pseudoaleatorios sean
    esenciales. Características clave incluyen reproducibilidad, periodo finito y eficiencia.
-   La elección de pseudoaleatorio se basa en la necesidad de reproducibilidad y control.
-   Por otro lado, la implementación de generadores aleatorios en el ámbito computacional presenta
    desafíos, ya que los ordenadores son sistemas deterministas y predecibles por naturaleza.
    Generar números verdaderamente aleatorios, impredecibles y sin patrones discernibles, como
    lanzar un dado o el ruido atmosférico, es un desafío. Incluso en situaciones peculiares, como la
    utilización de lámparas de lava por parte de Cloudflare, la búsqueda de fuentes naturales de
    aleatoriedad completamente impredecibles es complicada en el ámbito digital.
-   Java 17 introdujo mejoras significativas con la interfaz `RandomGenerator`, abordando la falta
    de una interfaz común.
-   Hemos modelados un generador con interfaz `Gen` que no deja de ser una simple función que acepta
    un `RandomGenerator` y produce un `Supplier` de objetos.
-   Métodos clave en `Gen` incluyen `sample()`, `map()`, `then()`, `classify()`, y `collect()`.

## ¡Manos a la obra! Creando algunos generadores...

-   Empecemos por definir el generador más sencillo: aquel que siempre produce el mismo valor.

```code

public interface Gen<O> extends Function<RandomGenerator, Supplier<O>> {

    static <O> Gen<O> cons(final O value) {
        return random -> () -> value;
    }

}

Gen<Integer> gen = Gen.cons(1);
Supplier<Integer> supplier = gen.apply(new Random());

var a = supplier.get();
var b = supplier.get();
var c = supplier.get();

```

Obviamente, en el ejemplo anterior, `a`,`b` y `c` son `1`.

Vamos a crear generadores de todos los tipos primitivos y definir una serie de operadores para
combinarlos y poder crear generadores más complejos. ¡De esta forma comprobaremos si nuestro modelo
funciona!

## Generadores de tipos primitivos

Empecemos por un generador de números enteros, que definiremos con la clase `IntGen`

```code

public final class IntGen implements Gen<Integer> {

    @Override
    public Supplier<Integer> apply(RandomGenerator random){
          return () -> random.nextInt();
    }

}


Gen<Integer> gen = new IntGen();
Supplier<Integer> supplier = gen.apply(new Random());

var a = supplier.get();
var b = supplier.get();
var c = supplier.get();


```

[1]-https://blog.cloudflare.com/lavarand-in-production-the-nitty-gritty-technical-details/
[2]-https://blogs.oracle.com/javamagazine/post/java-pseudorandom-number-generator-background
[3]-https://blogs.oracle.com/javamagazine/post/java-pseudo-random-number-generator-enhancements
