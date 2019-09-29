#!/usr/bin/env bash

#java  -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-scala-hash-map-100.log \
#-jar json-values-benchmark.jar -o jmh-log-immutable-scala-hash-map-100.txt  jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100\.scala_hash_map

#java   -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-scala-tree-map-100.log \
#-jar json-values-benchmark.jar -o jmh-log-immutable-scala-tree-map-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100\.scala_tree_map
#
#java   -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-clojure-hash-map-100.log \
#-jar json-values-benchmark.jar -o jmh-log-immutable-clojure-hash-map-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100\.clojure_hash_map
#
#java   -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-clojure_tree_map-100.log \
#-jar json-values-benchmark.jar -o jmh-log-immutable-clojure-tree-map-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100\.clojure_tree_map
#
#java   -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-clojure_array_map-100.log \
#-jar json-values-benchmark.jar -o jmh-log-immutable-clojure-array-map-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100\.clojure_array_map
#
#java   -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-vavr_hash_map-100.log \
#-jar json-values-benchmark.jar -o jmh-log-immutable-vavr-hash-map-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100\.vavr_hash_map
#
#java   -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-vavr_tree_map-100.log \
#-jar json-values-benchmark.jar -o jmh-log-immutable-vavr-tree-map-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100\.vavr_tree_map
#
#java   -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-pcollections_hash_map-100.log \
#-jar json-values-benchmark.jar -o jmh-log-immutable-pcollections-hash-map-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsObj_100\.pcollections_hash_map

#java   -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-jackson-object-100.log \
#-jar json-values-benchmark.jar -o jmh-log-mutable-jackson-object-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsObj_100\.jackson
#
#java   -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-java_hash_map-100.log \
#-jar json-values-benchmark.jar -o jmh-log-mutable-java-hash-map-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsObj_100\.java_hash_map

java   -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-ecollections_hash_map-100.log \
-jar json-values-benchmark.jar -o jmh-log-mutable-ecollections-hash-map-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsObj_100\.ecollections_hash_map