#!/usr/bin/env bash
java -Xms256m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-scala-hash-map-100.log \
-jar json-values-benchmark.jar -o jmh-log-immutable-scala-hash-map-100.txt  jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100\.scala_hash_map

java -Xms256m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-jackson-object-100.log \
-jar json-values-benchmark.jar -o jmh-log-mutable-jackson-object-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsObj_100\.jackson

java -Xms256m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-java_hash_map-100.log \
-jar json-values-benchmark.jar -o jmh-log-mutable-java-hash-map-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsObj_100\.java_hash_map