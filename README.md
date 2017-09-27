# JUnit 5 Samples [![ci-badge]][ci-travis]

Welcome to _JUnit 5 Samples_, a collection of sample applications using JUnit
Jupiter, JUnit Vintage and the JUnit Platform on various build systems.

CI builds for sample projects are available on [Jenkins][ci-jenkins] and
[Travis CI][ci-travis].


## Gradle Consumer ![badge-jdk-8] ![badge-tool-gradle] ![badge-junit-platform] ![badge-junit-jupiter] ![badge-junit-vintage]
The [junit5-gradle-consumer] sample demonstrates how to setup a Gradle project
using JUnit Platform, JUnit Jupiter and JUnit Vintage.


## Maven Consumer ![badge-jdk-8] ![badge-tool-maven] ![badge-junit-platform] ![badge-junit-jupiter] ![badge-junit-vintage]
The [junit5-maven-consumer] sample demonstrates how to setup a Maven project
using JUnit Platform, JUnit Jupiter and JUnit Vintage.


## Vanilla Jupiter on Gradle ![badge-jdk-8] ![badge-tool-gradle] ![badge-junit-jupiter]
The [junit5-vanilla-gradle] sample shows just the bare minimum to get started
with JUnit Jupiter on Gradle.


## Vanilla Jupiter on Maven ![badge-jdk-8] ![badge-tool-maven] ![badge-junit-jupiter]
The [junit5-vanilla-maven] sample shows just the bare minimum to get started
with JUnit Jupiter on Maven.


## Mockito Extension ![badge-jdk-8] ![badge-tool-gradle] ![badge-junit-platform] ![badge-junit-jupiter]
The [junit5-mockito-extension] sample shows how to integrate Mockito into JUnit
Jupiter tests somewhat simpler.


## Ice Cream Machine ![badge-jdk-9] ![badge-tool-gradle] ![badge-junit-platform]
The [junit5-java9-engine] sample demonstrates how to implement a custom
[TestEngine][guide-custom-engine] using the Java Platform Module System.



[junit5-gradle-consumer]: junit5-gradle-consumer
[junit5-maven-consumer]: junit5-maven-consumer
[junit5-vanilla-gradle]: junit5-vanilla-gradle
[junit5-vanilla-maven]: junit5-vanilla-maven
[junit5-mockito-extension]: junit5-mockito-extension
[junit5-java9-engine]: junit5-java9-engine

[badge-jdk-8]: https://img.shields.io/badge/jdk-8-yellow.svg "JDK-8"
[badge-jdk-9]: https://img.shields.io/badge/jdk-9-orange.svg "JDK-9 or higher"
[badge-tool-gradle]: https://img.shields.io/badge/tool-gradle-blue.svg "Gradle wrapper included"
[badge-tool-maven]: https://img.shields.io/badge/tool-maven-0440af.svg "Maven wrapper included"
[badge-junit-platform]: https://img.shields.io/badge/junit-platform-brightgreen.svg "JUnit Platform"
[badge-junit-jupiter]: https://img.shields.io/badge/junit-jupiter-green.svg "JUnit Jupiter Engine"
[badge-junit-vintage]: https://img.shields.io/badge/junit-vintage-yellowgreen.svg "JUnit Vintage Engine"

[ci-badge]: https://travis-ci.org/junit-team/junit5-samples.svg "Travis CI build status"
[ci-travis]: https://travis-ci.org/junit-team/junit5-samples
[ci-jenkins]: https://junit.ci.cloudbees.com/blue/organizations/jenkins/JUnit%205%20Samples/branches/

[guide-custom-engine]: http://junit.org/junit5/docs/current/user-guide/#launcher-api-engines-custom "Plugging in Your Own Test Engine"
