#!/usr/bin/env bash
java -Xms256m -XX:+PrintGCDetails  -Xloggc:gc-log-json-values-100.log \
-jar json-values-benchmark.jar -o jmh-log-json-values-100.txt  jsonvalues\.benchmark\.parsing\.ParsingStringToJsObj_100\.json_values

java -Xms256m -XX:+PrintGCDetails  -Xloggc:gc-log-jackson-object-100.log \
-jar json-values-benchmark.jar -o jmh-log-jackson-100.txt  jsonvalues\.benchmark\.parsing\.ParsingStringToJsObj_100\.jackson
