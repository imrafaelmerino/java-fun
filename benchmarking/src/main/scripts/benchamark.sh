#!/usr/bin/env bash
VERSION=11.1.1
java -Xms256m \
-jar releases/${VERSION}/benchmark-${VERSION}-zulu-8.jar \
-rff results/${VERSION}/zulu/deserializer/zulu-r8-c8.json \
-rf json \
jsonvalues\.benchmark\.JsDeserializers

#java -Xms256m \
#-jar releases/${VERSION}/benchmark-${VERSION}-open-8.jar \
#-rff results/${VERSION}/open/serializer/open-r9-c8.json \
#-rf json \
#jsonvalues\.benchmark\.JsSerializers
#
#java -Xms256m \
#-jar releases/${VERSION}/benchmark-${VERSION}-open-9.jar \
#-rff results/${VERSION}/open/deserializer/open-r9-c9.json \
#-rf json \
#jsonvalues\.benchmark\.JsDeserializers
#
#java -Xms256m \
#-jar releases/${VERSION}/benchmark-${VERSION}-open-9.jar \
#-rff results/${VERSION}/open/serializer/open-r9-c9.json \
#-rf json \
#jsonvalues\.benchmark\.JsSerializers

#java -Xms256m \
#-jar releases/${VERSION}/benchmark-${VERSION}-open-10.jar \
#-rff results/${VERSION}/open/deserializer/open-r11-c10.json \
#-rf json \
#jsonvalues\.benchmark\.JsDeserializers
#
#java -Xms256m \
#-jar releases/${VERSION}/benchmark-${VERSION}-open-10.jar \
#-rff results/${VERSION}/open/serializer/open-r11-c10.json \
#-rf json \
#jsonvalues\.benchmark\.JsSerializers
#
#java -Xms256m \
#-jar releases/${VERSION}/benchmark-${VERSION}-open-11.jar \
#-rff results/${VERSION}/open/deserializer/open-r11-c11.json \
#-rf json \
#jsonvalues\.benchmark\.JsDeserializers
#
#java -Xms256m \
#-jar releases/${VERSION}/benchmark-${VERSION}-open-11.jar \
#-rff results/${VERSION}/open/serializer/open-r11-c11.json \
#-rf json \
#jsonvalues\.benchmark\.JsSerializers

#java -Xms256m \
#-jar releases/${VERSION}/benchmark-${VERSION}-open-12.jar \
#-rff results/${VERSION}/open/deserializer/open-r11-c12.json \
#-rf json \
#jsonvalues\.benchmark\.JsDeserializers
#
#java -Xms256m \
#-jar releases/${VERSION}/benchmark-${VERSION}-open-12.jar \
#-rff results/${VERSION}/open/serializer/open-r11-c12.json \
#-rf json \
#jsonvalues\.benchmark\.JsSerializers
#
#java -Xms256m \
#-jar releases/${VERSION}/benchmark-${VERSION}-open-13.jar \
#-rff results/${VERSION}/open/deserializer/open-r11-c13.json \
#-rf json \
#jsonvalues\.benchmark\.JsDeserializers
#
#java -Xms256m \
#-jar releases/${VERSION}/benchmark-${VERSION}-open-13.jar \
#-rff results/${VERSION}/open/serializer/open-r11-c13.json \
#-rf json \
#jsonvalues\.benchmark\.JsSerializers
#
#java -Xms256m \
#-jar releases/${VERSION}/benchmark-${VERSION}-open-14.jar \
#-rff results/${VERSION}/open/deserializer/open-r11-c14.json \
#-rf json \
#jsonvalues\.benchmark\.JsDeserializers
#
#java -Xms256m \
#-jar releases/${VERSION}/benchmark-${VERSION}-open-14.jar \
#-rff results/${VERSION}/open/serializer/open-r11-c14.json \
#-rf json \
#jsonvalues\.benchmark\.JsSerializers

