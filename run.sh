#!/bin/sh

./gradlew :shadowJar
time java -jar build/libs/OneBillionRowChallengeKotlin-1.0-SNAPSHOT-all.jar "$1"