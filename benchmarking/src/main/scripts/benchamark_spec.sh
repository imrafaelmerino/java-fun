#!/usr/bin/env bash
java -Xms256m  \
-jar json-values-benchmark.jar   jsonvalues\.benchmark\.parsing\.ParseStringWithSpec\.json_values

java -Xms256m \
-jar json-values-benchmark.jar   jsonvalues\.benchmark\.parsing\.ParseStringWithSpec\.jackson


java -Xms256m  \
-jar json-values-benchmark.jar   jsonvalues\.benchmark\.parsing\.ParseStringWithSpec\.json_values_with_spec
