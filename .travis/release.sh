#!/usr/bin/env bash
set -e
echo "Generating changelog"
./mvnw exec:exec -Dexec.executable="scripts/changelog.sh"

echo "Ensuring that pom  matches $TRAVIS_TAG"
./mvnw org.codehaus.mojo:versions-maven-plugin:2.5:set -DnewVersion=${TRAVIS_TAG}

echo "Uploading to oss repo and GitHub"
./mvnw deploy --settings .travis/settings.xml -DskipTests=true --batch-mode --update-snapshots -Prelease