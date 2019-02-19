#!/bin/bash

set -o nounset
set -o errexit
set -o pipefail

###### Maven ######
${MVN} clean org.jacoco:jacoco-maven-plugin:prepare-agent verify sonar:sonar
