#!/bin/bash

#
# Set constants.
#
junit_platform_version='1.2.0'
ant_version='1.10.3'
ant_folder="apache-ant-${ant_version}"
ant_archive="${ant_folder}-bin.tar.gz"

#
# Load and extract Apache Ant.
#
wget --timestamping --continue "http://www.us.apache.org/dist/ant/binaries/${ant_archive}"
tar --extract --auto-compress --exclude "${ant_folder}/manual" --file "${ant_archive}"

#
# Load and store junit-platform-console-standalone jar in ${ANT_HOME}/lib.
#
wget --timestamping --continue --directory-prefix "${ant_folder}/lib" "http://central.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/${junit_platform_version}/junit-platform-console-standalone-${junit_platform_version}.jar"

#
# Finally, let Ant do its work...
#
"${ant_folder}/bin/ant"
