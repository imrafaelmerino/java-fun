#!/usr/bin/env bash
java -Xms256m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-scala_vector-1000.log \
-jar json-values-benchmark.jar -o jmh-log-immutable-scala_vector-1000.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsArray_1000\.scala_vector

java -Xms256m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-jackson-array-1000.log \
-jar json-values-benchmark.jar -o jmh-log-mutable-jackson-array-1000.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsArray_1000\.jackson

java -Xms256m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-java_array_list-1000.log \
-jar json-values-benchmark.jar -o jmh-log-mutable-java_array_list-1000.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsArray_1000\.java_array_list