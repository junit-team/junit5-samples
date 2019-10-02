#!/bin/bash

#
# Set constants.
#
junit_platform_version='1.5.2'
junit_jupiter_version='5.5.2'
ant_version='1.10.7'
ant_folder="apache-ant-${ant_version}"
ant_archive="${ant_folder}-bin.tar.gz"

#
# Load and extract Apache Ant.
#
curl --remote-name "https://archive.apache.org/dist/ant/binaries/${ant_archive}"
tar --extract -z --exclude "${ant_folder}/manual" --file "${ant_archive}"

#
# Load and store necessary JUnit5 jar into ${ANT_HOME}/lib.
#
(cd "${ant_folder}/lib" && curl --remote-name "http://central.maven.org/maven2/org/junit/platform/junit-platform-commons/${junit_platform_version}/junit-platform-commons-${junit_platform_version}.jar")
(cd "${ant_folder}/lib" && curl --remote-name "http://central.maven.org/maven2/org/junit/platform/junit-platform-engine/${junit_platform_version}/junit-platform-engine-${junit_platform_version}.jar")
(cd "${ant_folder}/lib" && curl --remote-name "http://central.maven.org/maven2/org/junit/platform/junit-platform-launcher/${junit_platform_version}/junit-platform-launcher-${junit_platform_version}.jar")
(cd "${ant_folder}/lib" && curl --remote-name "http://central.maven.org/maven2/org/junit/jupiter/junit-jupiter-api/${junit_jupiter_version}/junit-jupiter-api-${junit_jupiter_version}.jar")
(cd "${ant_folder}/lib" && curl --remote-name "http://central.maven.org/maven2/org/junit/jupiter/junit-jupiter-engine/${junit_jupiter_version}/junit-jupiter-engine-${junit_jupiter_version}.jar")
(cd "${ant_folder}/lib" && curl --remote-name "http://central.maven.org/maven2/org/junit/jupiter/junit-jupiter-params/${junit_jupiter_version}/junit-jupiter-params-${junit_jupiter_version}.jar")
(cd "${ant_folder}/lib" && curl --remote-name "http://central.maven.org/maven2/org/opentest4j/opentest4j/1.2.0/opentest4j-1.2.0.jar")
(cd "${ant_folder}/lib" && curl --remote-name "http://central.maven.org/maven2/org/apiguardian/apiguardian-api/1.1.0/apiguardian-api-1.1.0.jar")

#
# Finally, let Ant do its work...
#
ANT_HOME=${ant_folder} "./${ant_folder}/bin/ant"
