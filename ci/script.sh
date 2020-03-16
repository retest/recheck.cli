#!/bin/bash

set -o nounset
set -o errexit
set -o pipefail

###### Maven ######
if [ ${TRAVIS_SECURE_ENV_VARS} = "true" ]; then
    ${MVN} clean verify sonar:sonar -Pcoverage
else
    ${MVN} clean verify
fi
