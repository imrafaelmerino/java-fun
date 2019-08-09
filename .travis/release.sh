#!/usr/bin/env bash
set -e
mvn deploy --settings .travis/settings.xml -DskipTests=true -B -U -Prelease