#!/bin/bash

#
# Set constants.
#
junit_platform_version='1.7.0'
ant_version='1.10.7'
ant_folder="apache-ant-${ant_version}"
ant_archive="${ant_folder}-bin.tar.gz"

#
# Load and extract Apache Ant.
#
curl --remote-name "https://archive.apache.org/dist/ant/binaries/${ant_archive}"
tar --extract -z --exclude "${ant_folder}/manual" --file "${ant_archive}"

#
# Load and store junit-platform-console-standalone jar into ${ANT_HOME}/lib.
#
(cd "${ant_folder}/lib" && curl --remote-name  "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/${junit_platform_version}/junit-platform-console-standalone-${junit_platform_version}.jar")

#
# Finally, let Ant do its work...
#
ANT_HOME=${ant_folder} "./${ant_folder}/bin/ant"
