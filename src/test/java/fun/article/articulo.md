https://blogs.oracle.com/javamagazine/post/java-pseudo-random-number-generator-enhancements
https://blog.cloudflare.com/randomness-101-lavarand-in-production/


###         

Vamos a explicar como implementar una librería de generación de números pseudoaleatorios (PRG), inspirada en los
principios de la programación funcional. En esta aventura, conceptualizaremos lo que llamaremos 'Generador', 
diseñaremos generadores para tipos primitivos y, finalmente, crearemos generadores de estructuras más complejas, como 
Json, Records y objetos de cualquier clase Java.

La utilización de generadores en el proceso de testing proporciona una serie de ventajas sustanciales que pueden mejorar
significativamente la eficacia y la calidad de las pruebas. Aquí se detallan algunas de las innumerables ventajas:


- **Fácil Generación de Datos de Prueba:**Los generadores simplifican la creación de datos de prueba al automatizar el proceso. En lugar de tener que definir
  manualmente cada conjunto de datos, los generadores permiten especificar reglas y criterios, generando automáticamente
  casos de prueba.

### 2. **Mantenibilidad Mejorada:**

- La modularidad inherente a los generadores facilita la adaptación y actualización de casos de prueba. Al centralizar
  la lógica de generación en un generador específico, cualquier cambio en los requisitos de prueba puede abordarse de
  manera más eficiente y coherente.

### 3. **Random Testing (Pruebas Aleatorias):**

- La capacidad de generar datos de prueba de manera aleatoria a través de generadores es valiosa para descubrir posibles
  problemas que podrían no ser evidentes en pruebas más estáticas. Este enfoque aleatorio puede revelar escenarios
  inesperados y mejorar la robustez de la aplicación.

### 4. **Property-Based Testing (Pruebas Basadas en Propiedades):**

- Los generadores son fundamentales para la implementación de pruebas basadas en propiedades. En lugar de centrarse en
  casos de prueba específicos, estas pruebas definen propiedades que deben cumplir todas las entradas generadas por un
  generador. Esto facilita la identificación de errores y comportamientos inesperados en un conjunto amplio de datos.

### 5. **Mayor Cobertura de Código:**

- La automatización de la generación de datos permite realizar pruebas exhaustivas con una amplia variedad de entradas,
  aumentando la probabilidad de descubrir problemas en diferentes partes del código.

### 6. **Reproducibilidad:**

- La capacidad de reproducir casos de prueba específicos mediante la fijación de semillas en generadores asegura la
  consistencia en las pruebas. Esto es especialmente valioso para la depuración y la colaboración en entornos de
  desarrollo.

### 7. **Eficiencia en el Desarrollo de Pruebas:**

- Al utilizar generadores, se reduce la carga de trabajo manual asociada con la creación y mantenimiento de conjuntos de
  datos de prueba, lo que permite a los desarrolladores y equipos de QA centrarse en la calidad y eficacia de las
  pruebas en lugar de la manipulación directa de los datos de prueba.

### 8. **Facilita la Adopción de Técnicas Avanzadas de Testing:**

- Los generadores sirven como base para técnicas avanzadas como pruebas basadas en propiedades y pruebas aleatorias, lo
  que eleva el nivel de sofisticación de las estrategias de testing implementadas.

En resumen, la utilización de generadores en el testing no solo simplifica el proceso, sino que también proporciona un
enfoque más robusto y eficiente para garantizar la calidad del software.

### ¿Aleatorio o Pseudoaleatorio? Entendiendo las Diferencias

La generación de números aleatorios es un concepto intrigante y esencial en el ámbito de la programación. Sin embargo,
antes de sumergirnos en los detalles de nuestra biblioteca de generadores pseudoaleatorios en Java, es crucial
comprender la diferencia fundamental entre los números verdaderamente aleatorios y aquellos que son pseudoaleatorios.

#### Generadores de Números Aleatorios:

En un sentido puro, los números aleatorios son impredecibles y no siguen ningún patrón discernible. Son el resultado de
eventos naturales o procesos caóticos, como el lanzamiento de un dado o el ruido atmosférico. En el ámbito
computacional, generar números verdaderamente aleatorios es un desafío, ya que las computadoras son sistemas
deterministas y predecibles por naturaleza.

#### Generadores Pseudoaleatorios:

Dada la naturaleza determinista de las computadoras, se recurre a los generadores pseudoaleatorios para simular la
aleatoriedad. Estos generadores utilizan algoritmos para producir secuencias de números que, aunque parecen aleatorios,
son completamente predecibles si se conoce la semilla inicial. La "semilla" es un valor de entrada que inicia el proceso
de generación y determina completamente la secuencia resultante.

#### Características Clave de los Generadores Pseudoaleatorios:

1. **Reproducibilidad:** Una misma semilla siempre generará la misma secuencia de números, lo que es beneficioso para
   pruebas y depuración.

2. **Ciclo Periódico:** Dado que los algoritmos son finitos, las secuencias eventualmente se repiten, formando un ciclo.
   La longitud de este ciclo depende del algoritmo y la semilla.

3. **Eficiencia:** A diferencia de los verdaderamente aleatorios, los generadores pseudoaleatorios son eficientes y
   pueden generar secuencias en cualquier momento.

### ¿Por qué Pseudoaleatorio y no Aleatorio?

La elección de utilizar generadores pseudoaleatorios en lugar de números aleatorios reales radica en la necesidad de
reproducibilidad y control. En aplicaciones prácticas, como simulaciones y juegos, es crucial poder replicar resultados
y compartir escenarios específicos, lo que se logra fácilmente con generadores pseudoaleatorios.

### ¿Cómo modelamos un generador en Java?

En Java ya existe la clase `Random` para producir sequencias pseudoaleatorias a partir the una semilla
inicial. 
En la versión 17 se introdujo la interfaz RandomGenerator




