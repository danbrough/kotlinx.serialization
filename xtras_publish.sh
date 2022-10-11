#!/bin/bash
cd `dirname "$0"`

TASKS="publishAllPublicationsToSonaTypeRepository"
[ -z "$SONATYPE_USER" ] && echo SONATYPE_USER not set && exit 1
[ -z "$SONATYPE_PASSWORD" ] && echo SONATYPE_PASSWORD not set && exit 1
[ -z "$SONATYPE_REPO_ID" ] && echo SONATYPE_REPO_ID not set && exit 1



OPTS="-PSONATYPE_REPO_ID=$SONATYPE_REPO_ID \
 -PmavenCentralUsername=$SONATYPE_USER -PmavenCentralPassword=$SONATYPE_PASSWORD \
 -PsignPublications -PpublishDocs "

if [ "$(uname)" = "Darwin" ]; then
	TASKS="$(cat mac_targets.txt)"
fi

./gradlew $OPTS $TASKS


