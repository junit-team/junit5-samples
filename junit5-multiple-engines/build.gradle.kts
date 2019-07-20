plugins {
    java
    eclipse // optional (to generate Eclipse project files)
    idea // optional (to generate IntelliJ IDEA project files)
    kotlin("jvm") version "1.3.11"
}

repositories {
    mavenCentral()
    jcenter {
        content {
            includeGroup("org.spekframework.spek2")
        }
    }
}

dependencies {
    val junit4Version = "4.12"
    val junitBomVersion = "5.5.1"

    // Using multiple engines in a single project may lead to different JUnit
    // Platform artifact versions being pulled in via transitive dependencies.
    // Gradle allows you to enforce usage of fixed set of versions that match.
    // https://docs.gradle.org/current/userguide/managing_transitive_dependencies.html#sec:bom_import
    implementation(enforcedPlatform("org.junit:junit-bom:$junitBomVersion")) {
        because("enforce matching Platform, Jupiter, and Vintage versions")
    }

    // JUnit Jupiter
    testImplementation("org.junit.jupiter:junit-jupiter")

    // JUnit Vintage
    testImplementation("junit:junit:$junit4Version")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine") {
        because("allows JUnit 3 and JUnit 4 tests to run")
    }

    // JUnit Platform Launcher + Console
    testRuntimeOnly("org.junit.platform:junit-platform-launcher") {
        because("allows tests to run from IDEs that bundle older version of launcher")
    }
    testRuntimeOnly("org.junit.platform:junit-platform-console") {
        because("needed to launch the JUnit Platform Console program")
    }

    // Mainrunner
    testImplementation("de.sormuras.mainrunner:de.sormuras.mainrunner.engine:2.0.2") {
        because("executes Java programs as tests")
    }

    // jqwik
    testImplementation("net.jqwik:jqwik:1.0.0") {
        because("allows jqwik properties to run")
    }

    // Spek2
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:2.0.0-rc.1") {
        exclude(module = "kotlin-stdlib-jdk8").because("we want to override the Kotlin version")
    }
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:2.0.0-rc.1") {
        because("allows Spek2 tests to be found and executed")
    }

    // KotlinTest
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.2.0")
    testRuntimeOnly("org.slf4j:slf4j-nop:1.7.25") {
        because("defaulting to no-operation (NOP) logger implementation")
    }
}

tasks {

    val consoleLauncherTest by registering(JavaExec::class) {
        dependsOn(testClasses)
        val reportsDir = file("$buildDir/test-results")
        outputs.dir(reportsDir)
        classpath = sourceSets.test.get().runtimeClasspath
        main = "org.junit.platform.console.ConsoleLauncher"
        args("--scan-classpath")
        args("--details", "tree")
        args("--reports-dir", reportsDir)
    }

    test {
        // useJUnitPlatform() ... https://github.com/gradle/gradle/issues/4912
        dependsOn(consoleLauncherTest)
        exclude("**/*")
    }
}
