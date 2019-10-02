#!/usr/bin/env bash
java -Xms256m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-scala-hash-map-1000.log \
-jar json-values-benchmark.jar -o jmh-log-immutable-scala-hash-map-1000.txt  jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_1000\.scala_hash_map

java -Xms256m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-jackson-object-1000.log \
-jar json-values-benchmark.jar -o jmh-log-mutable-jackson-object-1000.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsObj_1000\.jackson

java -Xms256m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-java_hash_map-1000.log \
-jar json-values-benchmark.jar -o jmh-log-mutable-java-hash-map-1000.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsObj_1000\.java_hash_map