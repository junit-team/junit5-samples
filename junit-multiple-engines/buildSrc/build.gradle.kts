import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget = JVM_11
    }
    compileJava {
        options.release.set(11)
    }
}
