#!/usr/bin/env bash
java -Xms256m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-scala_vector-100.log \
-jar json-values-benchmark.jar -o jmh-log-immutable-scala_vector-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsArray_100\.scala_vector

java -Xms256m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-jackson-array-100.log \
-jar json-values-benchmark.jar -o jmh-log-mutable-jackson-array-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsArray_100\.jackson

java -Xms256m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-java_array_list-100.log \
-jar json-values-benchmark.jar -o jmh-log-mutable-java_array_list-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsArray_100\.java_array_list