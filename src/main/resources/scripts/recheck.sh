#!/usr/bin/env bash

SCRIPT_DIR="$( ( cd "$( dirname "$0" )" && pwd -P ) )"
JAR=${SCRIPT_DIR}/../lib/recheck-cli.jar

JAVA=java

exec ${JAVA} -jar "$JAR" "$@"