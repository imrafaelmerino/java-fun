#!/usr/bin/env bash

java -Xms256m -Djmh.ignoreLock=true -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-scala-hash-map-100.log \
-jar json-values-benchmark.jar -o jmh-log-immutable-scala-hash-map-100.txt  jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100_concurrent\.scala_hash_map

java -Xms256m -Djmh.ignoreLock=true -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-jackson-object-100.log \
-jar json-values-benchmark.jar -o jmh-logmutable-jackson-object-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsObj_100_concurrent\.jackson

java -Xms256m -Djmh.ignoreLock=true -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-java_hash_map-100.log \
-jar json-values-benchmark.jar -o jmh-logmutable-java-hash-map-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsObj_100_concurrent\.java_hash_map
