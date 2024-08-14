#!/usr/bin/env bash

#
# Set constants.
#
junit_platform_version='1.11.0'
ant_version='1.10.13'
ant_folder="apache-ant-${ant_version}"
ant_archive="${ant_folder}-bin.tar.gz"
standalone_jar="${ant_folder}/lib/junit-platform-console-standalone-${junit_platform_version}.jar"

#
# Load and extract Apache Ant.
#
if [ ! -d "${ant_folder}" ]; then
    curl --remote-name "https://archive.apache.org/dist/ant/binaries/${ant_archive}"
    tar --extract -z --exclude "${ant_folder}/manual" --file "${ant_archive}"
fi

#
# Load and store junit-platform-console-standalone jar into ${ANT_HOME}/lib.
#
if [ ! -f "${standalone_jar}" ]; then
    curl "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/${junit_platform_version}/junit-platform-console-standalone-${junit_platform_version}.jar" \
         --output "${standalone_jar}"
fi

#
# Finally, let Ant do its work...
#
ANT_HOME=${ant_folder} "./${ant_folder}/bin/ant"
