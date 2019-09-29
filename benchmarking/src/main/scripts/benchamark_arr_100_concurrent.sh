#!/usr/bin/env bash
java -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-scala_vector-100.log \
-jar json-values-benchmark.jar -o jmh-logimmutable-scala_vector-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsArray_100_concurrent\.scala_vector

java -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-scala_list-100.log \
-jar json-values-benchmark.jar -o jmh-logimmutable-scala_list-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsArray_100_concurrent\.scala_list

java -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-clojure_vector-100.log \
-jar json-values-benchmark.jar -o jmh-log-immutable-clojure_vector-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsArray_100_concurrent\.clojure_vector

java -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-clojure_set-100.log \
-jar json-values-benchmark.jar -o jmh-log-immutable-clojure_set-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsArray_100_concurrent\.clojure_set

java -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-vavr_vector-100.log \
-jar json-values-benchmark.jar -o jmh-log-immutable-clojure-vavr_vector-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsArray_100_concurrent\.vavr_vector

java -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-vavr_list-100.log \
-jar json-values-benchmark.jar -o jmh-log-immutable-vavr_list-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsArray_100_concurrent\.vavr_list

java -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-vavr_set-100.log \
-jar json-values-benchmark.jar -o jmh-log-immutable-vavr_set-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsArray_100_concurrent\.vavr_set

java -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-immutable-pcollections_vector-100.log \
-jar json-values-benchmark.jar -o jmh-log-immutable-pcollections_vector-100.txt jsonvalues\.benchmark\.immutable\.parsing\.StringToJsArray_100_concurrent\.pcollections_vector

java -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-jackson-array-100.log \
-jar json-values-benchmark.jar -o jmh-log-mutable-jackson-array-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsArray_100_concurrent\.jackson

java -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-java_array_list-100.log \
-jar json-values-benchmark.jar -o jmh-log-mutable-java_array_list-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsArray_100_concurrent\.java_array_list

java -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-java_hash_set-100.log \
-jar json-values-benchmark.jar -o jmh-log-mutable-java_hash_set-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsArray_100_concurrent\.java_hash_set

java -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc-log-mutable-ecollections_list-100.log \
-jar json-values-benchmark.jar -o jmh-log-mutable-ecollections_list-100.txt jsonvalues\.benchmark\.mutable\.parsing\.StringToJsArray_100_concurrent\.ecollections_list