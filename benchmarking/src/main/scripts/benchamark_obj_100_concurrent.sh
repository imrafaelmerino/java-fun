#!/usr/bin/env bash

java  -Djmh.ignoreLock=true -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-scala-hash-map-100.log \
-jar json-values-benchmark.jar -o jmh-log-immutable-scala-hash-map-100.txt  jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100_concurrent\.scala_hash_map

#java -Djmh.ignoreLock=true  -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-scala-tree-map-100.log \
#-jar json-values-benchmark.jar -o jmh-log-immutable-scala-tree-map-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100_concurrent\.scala_tree_map

#java -Djmh.ignoreLock=true -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-clojure-hash-map-100.log \
#-jar json-values-benchmark.jar -o jmh-log-immutable-clojure-hash-map-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100_concurrent\.clojure_hash_map
#
#java -Djmh.ignoreLock=true -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-clojure_tree_map-100.log \
#-jar json-values-benchmark.jar -o jmh-log-immutable-clojure-tree-map-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100_concurrent\.clojure_tree_map
#
#java -Djmh.ignoreLock=true -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-clojure_array_map-100.log \
#-jar json-values-benchmark.jar -o jmh-log-immutable-clojure-array-map-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100_concurrent\.clojure_array_map
#
#java -Djmh.ignoreLock=true -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-vavr_hash_map-100.log \
#-jar json-values-benchmark.jar -o jmh-log-immutable-vavr-hash-map-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100_concurrent\.vavr_hash_map
#
#java -Djmh.ignoreLock=true -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-vavr_tree_map-100.log \
#-jar json-values-benchmark.jar -o jmh-log-immutable-vavr-tree-map-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100_concurrent\.vavr_tree_map
#
#java -Djmh.ignoreLock=true -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-pcollections_hash_map-100.log \
#-jar json-values-benchmark.jar -o jmh-log-immutable-pcollections-hash-map-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100_concurrent\.pcollections_hash_map

java -Djmh.ignoreLock=true -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-jackson-object-100.log \
-jar json-values-benchmark.jar -o jmh-logmutable-jackson-object-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsObj_100_concurrent\.jackson

java -Djmh.ignoreLock=true -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-java_hash_map-100.log \
-jar json-values-benchmark.jar -o jmh-logmutable-java-hash-map-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsObj_100_concurrent\.java_hash_map

#java -Djmh.ignoreLock=true -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-ecollections_hash_map-100.log \
#-jar json-values-benchmark.jar -o jmh-logmutable-ecollections-hash-map-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsObj_100_concurrent\.ecollections_hash_map
