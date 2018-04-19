#!/bin/bash

#
# Set constants.
#
junit_platform_version='1.1.1'
ant_version='1.10.3'
ant_folder="apache-ant-${ant_version}"
ant_archive="${ant_folder}-bin.tar.gz"

#
# Load and extract Apache Ant.
#
wget "http://www.us.apache.org/dist/ant/binaries/${ant_archive}"
tar --extract --auto-compress --exclude '.*/manual' --file "${ant_archive}"

#
# Load and store junit-platform-console-standalone jar in ${ANT_HOME}/lib.
#
wget --directory-prefix "${ant_folder}/lib" "http://central.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/${junit_platform_version}/junit-platform-console-standalone-${junit_platform_version}.jar"

#
# Finally, let Ant do its work...
#
"${ant_folder}/bin/ant"
