#!/usr/bin/env bash
#git log <last release> HEAD --grep feat
#git log <last release> HEAD --grep fix
#git log <last release> HEAD --grep "BREAKING CHANGE"

CURRENT_DIR=$(dirname "$0")
git log --pretty="%h - %s (%an)" master v0.1.0 HEAD > ${CURRENT_DIR}/../docs/CHANGELOG.md