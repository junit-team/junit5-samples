# JUnit 5 Samples [![ci-badge]][ci-travis]

Welcome to _JUnit 5 Samples_, a collection of sample applications and extensions
using JUnit Jupiter, JUnit Vintage, and the JUnit Platform on various build systems.

CI builds for sample projects are available on [Jenkins][ci-jenkins] and
[Travis CI][ci-travis].

## Vanilla Jupiter on Gradle ![badge-jdk-8] ![badge-tool-gradle] ![badge-junit-jupiter]

The [junit5-vanilla-gradle] sample demonstrates the bare minimum configuration for
getting started with JUnit Jupiter using the Gradle build system.

## Vanilla Jupiter on Maven ![badge-jdk-8] ![badge-tool-maven] ![badge-junit-jupiter]

The [junit5-vanilla-maven] sample demonstrates the bare minimum configuration for
getting started with JUnit Jupiter using the Maven build system.

## Vanilla Jupiter on JShell ![badge-jdk-9] ![badge-tool-console] ![badge-junit-jupiter]

The [junit5-vanilla-modules] sample demonstrates how to test code organized in modules.

## Gradle Consumer ![badge-jdk-8] ![badge-tool-gradle] ![badge-junit-platform] ![badge-junit-jupiter] ![badge-junit-vintage]

The [junit5-gradle-consumer] sample demonstrates how to set up a Gradle project
using the JUnit Platform, JUnit Jupiter, and JUnit Vintage.

## Maven Consumer ![badge-jdk-8] ![badge-tool-maven] ![badge-junit-platform] ![badge-junit-jupiter] ![badge-junit-vintage]

The [junit5-maven-consumer] sample demonstrates how to set up a Maven project
using the JUnit Platform, JUnit Jupiter, and JUnit Vintage.

## Mockito Extension ![badge-jdk-8] ![badge-tool-gradle] ![badge-junit-platform] ![badge-junit-jupiter]

The [junit5-mockito-extension] sample provides a simple extension that demonstrates
how one can integrate Mockito into JUnit Jupiter tests.

## Ice Cream Machine ![badge-jdk-9] ![badge-tool-gradle] ![badge-junit-platform]

The [junit5-java9-engine] sample demonstrates how to implement a custom
[TestEngine][guide-custom-engine] for the JUnit Platform using the Java
Platform Module System (a.k.a., _Project Jigsaw_ for Java 9).


[junit5-gradle-consumer]: junit5-gradle-consumer
[junit5-maven-consumer]: junit5-maven-consumer
[junit5-vanilla-gradle]: junit5-vanilla-gradle
[junit5-vanilla-maven]: junit5-vanilla-maven
[junit5-vanilla-modules]: junit5-vanilla-modules
[junit5-mockito-extension]: junit5-mockito-extension
[junit5-java9-engine]: junit5-java9-engine

[badge-jdk-8]: https://img.shields.io/badge/jdk-8-yellow.svg "JDK-8"
[badge-jdk-9]: https://img.shields.io/badge/jdk-9-orange.svg "JDK-9"
[badge-jdk-10]: https://img.shields.io/badge/jdk-10-red.svg "JDK-10 or higher"
[badge-tool-gradle]: https://img.shields.io/badge/tool-gradle-blue.svg "Gradle wrapper included"
[badge-tool-maven]: https://img.shields.io/badge/tool-maven-0440af.svg "Maven wrapper included"
[badge-tool-console]: https://img.shields.io/badge/tool-console-022077.svg "Command line tools"
[badge-junit-platform]: https://img.shields.io/badge/junit-platform-brightgreen.svg "JUnit Platform"
[badge-junit-jupiter]: https://img.shields.io/badge/junit-jupiter-green.svg "JUnit Jupiter Engine"
[badge-junit-vintage]: https://img.shields.io/badge/junit-vintage-yellowgreen.svg "JUnit Vintage Engine"

[ci-badge]: https://travis-ci.org/junit-team/junit5-samples.svg "Travis CI build status"
[ci-travis]: https://travis-ci.org/junit-team/junit5-samples
[ci-jenkins]: https://junit.ci.cloudbees.com/blue/organizations/jenkins/JUnit%205%20Samples/branches/

[guide-custom-engine]: http://junit.org/junit5/docs/current/user-guide/#launcher-api-engines-custom "Plugging in Your Own Test Engine"
