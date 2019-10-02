#!/usr/bin/env bash
java -Xms256m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-scala_vector-100.log \
-jar json-values-benchmark.jar -o jmh-logimmutable-scala_vector-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsArray_100_concurrent\.scala_vector

java -Xms256m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-jackson-array-100.log \
-jar json-values-benchmark.jar -o jmh-log-mutable-jackson-array-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsArray_100_concurrent\.jackson

java -Xms256m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-java_array_list-100.log \
-jar json-values-benchmark.jar -o jmh-log-mutable-java_array_list-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsArray_100_concurrent\.java_array_list
