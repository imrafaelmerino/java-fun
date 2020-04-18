#!/usr/bin/env bash

java -Xms256m  \
-jar json-values-benchmark.jar   jsonvalues\.benchmark\.serializing\.SerializingJsObj\.json_values

java -Xms256m  \
-jar json-values-benchmark.jar   jsonvalues\.benchmark\.serializing\.SerializingJsObj\.jackson
